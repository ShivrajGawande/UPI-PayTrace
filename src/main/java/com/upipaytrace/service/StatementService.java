package com.upipaytrace.service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.upipaytrace.entity.BankAccount;
import com.upipaytrace.entity.Transaction;
import com.upipaytrace.entity.User;
import com.upipaytrace.parser.StatementParser;
import com.upipaytrace.repository.BankAccountRepository;
import com.upipaytrace.repository.TransactionRepository;
import com.upipaytrace.repository.UserRepository;
import com.upipaytrace.util.AmountParserUtil;
import com.upipaytrace.util.DateParserUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StatementService {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    
    private final List<StatementParser> parsers;


    public String processStatement(MultipartFile file, Long bankAccountId, Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        BankAccount bankAccount = bankAccountRepository
                .findByIdAndUser(bankAccountId, user)
                .orElseThrow(() -> new RuntimeException("Bank Account not Found"));

        StatementParser parser = parsers.stream()
                .filter(p -> p.supports(file.getOriginalFilename()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unsupported file format"));

        List<Map<String, String>> rows = parser.parse(file);


        int totalRows = 0, upiRows = 0, saved = 0, duplicates = 0, invalid = 0;

        for (Map<String, String> row : rows) {

            totalRows++;

            try {
                String narration = row.getOrDefault("Narration", "");

                if (!narration.toUpperCase().contains("UPI"))
                    continue;

                upiRows++;

                Transaction transaction = new Transaction();

                transaction.setReferenceText(narration);
                transaction.setTransactionDate(
                        DateParserUtil.parse(row.get("Date"))
                );


                String debit = row.getOrDefault("Debit", "");
                String credit = row.getOrDefault("Credit", "");

                BigDecimal amount = !debit.isEmpty()?AmountParserUtil.parse(debit): AmountParserUtil.parse(credit);

                transaction.setAmount(amount);

                boolean exists = transactionRepository
                        .existsByBankAccountAndTransactionDateAndAmountAndReferenceText(
                                bankAccount,
                                transaction.getTransactionDate(),
                                transaction.getAmount(),
                                transaction.getReferenceText()
                        );

                if (exists) {
                    duplicates++;
                    continue;
                }

                transaction.setBankAccount(bankAccount);
                transactionRepository.save(transaction);
                saved++;

            } catch (Exception e) {
                invalid++;
            }
        }

        return "CSV processed successfully | Total: " + totalRows +", UPI: " + upiRows + ", Saved: " + saved +
               ", Duplicates: " + duplicates +", Invalid: " + invalid;
    }

}
