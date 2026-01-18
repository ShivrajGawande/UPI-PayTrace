package com.upipaytrace.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upipaytrace.dto.BankAccountRequestDto;
import com.upipaytrace.dto.BankAccountResponceDto;
import com.upipaytrace.service.BankAccountService;

import lombok.AllArgsConstructor;
@RestController
@AllArgsConstructor
@RequestMapping("/api/bank-accounts")
public class BankAccountController {

    private final BankAccountService bankService;

    @PostMapping
    public ResponseEntity<String> addAccount(@RequestBody BankAccountRequestDto bankAcc,Principal principal) {

        String msg = bankService.addBankAccount(bankAcc, principal);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BankAccountResponceDto>> getAccounts(Principal principal) {

        List<BankAccountResponceDto> list = bankService.getUserBankAccounts(principal);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponceDto> getAccountById(@PathVariable Long id, Principal principal) {

        BankAccountResponceDto bankAcc = bankService.getBankAccountById(id, principal);
        return ResponseEntity.ok(bankAcc);
    }
}
