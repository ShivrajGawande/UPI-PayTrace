package com.upipaytrace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upipaytrace.entity.BankAccount;
import com.upipaytrace.entity.User;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findByUser(User user);
    Optional<BankAccount> findByIdAndUser(Long id, User user);
    boolean existsByUserAndMaskedAccountNumber(User user, String maskedAccNo);
}

