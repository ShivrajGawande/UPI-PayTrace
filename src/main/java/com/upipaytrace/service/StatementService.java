package com.upipaytrace.service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.upipaytrace.entity.BankAccount;
import com.upipaytrace.entity.Transaction;
import com.upipaytrace.entity.User;
import com.upipaytrace.repository.BankAccountRepository;
import com.upipaytrace.repository.TransactionRepository;
import com.upipaytrace.repository.UserRepository;
import com.upipaytrace.util.CsvParserUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StatementService {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    public String processCsv(MultipartFile file, Long bankAccountId, Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        BankAccount bankAccount = bankAccountRepository
                .findByIdAndUser(bankAccountId, user)
                .orElseThrow(() -> new RuntimeException("Bank Account not Found"));

        List<Map<String, String>> rows = CsvParserUtil.parse(file);

        for (Map<String, String> row : rows) {

            String narration = row.getOrDefault("Narration", "");

            if (!narration.toUpperCase().contains("UPI")) {
                continue;
            }

            Transaction transaction = new Transaction();

            transaction.setReferenceText(narration);
            transaction.setTransactionDate(LocalDate.parse(row.get("Date")));

            transaction.setAmount(
                    row.get("Debit").isEmpty()
                            ? new BigDecimal(row.get("Credit"))
                            : new BigDecimal(row.get("Debit"))
            );

            boolean exists = transactionRepository
                    .existsByBankAccountAndTransactionDateAndAmountAndReferenceText(
                            bankAccount,
                            transaction.getTransactionDate(),
                            transaction.getAmount(),
                            transaction.getReferenceText()
                    );

            if (exists) {
                continue;
            }

            transaction.setBankAccount(bankAccount);
            transactionRepository.save(transaction);
        }

        return "CSV processed successfully";
    }
}
