package com.upipaytrace.service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.upipaytrace.dto.TransactionRequestDto;
import com.upipaytrace.dto.TransactionResponseDto;
import com.upipaytrace.entity.BankAccount;
import com.upipaytrace.entity.Transaction;
import com.upipaytrace.entity.User;
import com.upipaytrace.repository.BankAccountRepository;
import com.upipaytrace.repository.TransactionRepository;
import com.upipaytrace.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    public String saveTransaction(TransactionRequestDto transactionDto, Long bankAccountId, Principal principal) {

    		User user = userRepository.findByEmail(principal.getName())
				.orElseThrow(()-> new RuntimeException("User Not Found"));
    		BankAccount bankAccount = bankAccountRepository.findByIdAndUser(bankAccountId, user).
        		orElseThrow(() -> new RuntimeException("Bank Account not Found"));
        
    		Transaction transaction = new Transaction();
    		BeanUtils.copyProperties(transactionDto, transaction);
        
        boolean exists = transactionRepository
                .existsByBankAccountAndTransactionDateAndAmountAndReferenceText(
                        bankAccount,
                        transaction.getTransactionDate(),
                        transaction.getAmount(),
                        transaction.getReferenceText());

        if (exists) {
            throw new RuntimeException("Duplicate transaction detected");
        }

        transaction.setBankAccount(bankAccount);
        transactionRepository.save(transaction);
        return "Transaction Saved Succeessfully..";
    }

    public List<TransactionResponseDto> getTransactions(Long bankAccountId, Principal principal) {
    	
    		User user = userRepository.findByEmail(principal.getName())
    				.orElseThrow(()-> new RuntimeException("User Not Found"));
        BankAccount bankAccount = bankAccountRepository.findByIdAndUser(bankAccountId, user).
        		orElseThrow(() -> new RuntimeException("BankAccout not Found"));
        
        List<Transaction> transactionList = transactionRepository.findByBankAccount(bankAccount);
        List<TransactionResponseDto> transactionDtoList = new ArrayList<>();
        
        for(Transaction transaction : transactionList)
        {
        		TransactionResponseDto dto = new TransactionResponseDto();
        		BeanUtils.copyProperties(transaction, dto);
        		
        		transactionDtoList.add(dto);
        }
        
        return transactionDtoList;
    }

    public List<TransactionResponseDto> getTransactionsByDateRange(
            Long bankAccountId, LocalDate startDate, LocalDate endDate, Principal principal) {

    			User user = userRepository.findByEmail(principal.getName())
				.orElseThrow(()-> new RuntimeException("User Not Found"));
    			
    			BankAccount bankAccount = bankAccountRepository.findByIdAndUser(bankAccountId, user).
    					orElseThrow(() -> new RuntimeException("Bank Account not Found"));
    			
    	        List<Transaction> transactionList = transactionRepository.
    	        		findByBankAccountAndTransactionDateBetween(bankAccount,startDate,endDate);
    	        
    	        List<TransactionResponseDto> transactionDtoList = new ArrayList<>();
    	        
    	        for(Transaction transaction : transactionList)
    	        {
    	        		TransactionResponseDto dto = new TransactionResponseDto();
    	        		BeanUtils.copyProperties(transaction, dto);
    	        		
    	        		transactionDtoList.add(dto);
    	        }
    	        
    	        return transactionDtoList;
    }

}
