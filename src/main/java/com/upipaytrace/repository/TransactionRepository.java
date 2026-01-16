package com.upipaytrace.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upipaytrace.entity.BankAccount;
import com.upipaytrace.entity.Transaction;
import com.upipaytrace.enums.UpiAppSource;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    List<Transaction> findByBankAccount(BankAccount bankAccount);

    List<Transaction> findByBankAccountAndTransactionDateBetween(BankAccount bankAccount,
            														LocalDate startDate, LocalDate endDate);

    List<Transaction> findByBankAccountAndUpiAppSource(BankAccount bankAccount,UpiAppSource upiAppSource);

    boolean existsByBankAccountAndTransactionDateAndAmountAndReferenceText(BankAccount bankAccount,
            LocalDate transactionDate,BigDecimal amount,String referenceText
    );
}
