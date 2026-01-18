package com.upipaytrace.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upipaytrace.dto.TransactionRequestDto;
import com.upipaytrace.dto.TransactionResponseDto;
import com.upipaytrace.service.TransactionService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/{bankAccountId}")
    public ResponseEntity<String> saveTransaction(@PathVariable Long bankAccountId,
            @RequestBody TransactionRequestDto transaction, Principal principal) {

        String msg = transactionService.saveTransaction(transaction, bankAccountId, principal);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/{bankAccountId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactions(@PathVariable Long bankAccountId,Principal principal) {

        List<TransactionResponseDto> list =
                transactionService.getTransactions(bankAccountId, principal);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{bankAccountId}/date-range")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByDateRange(@PathVariable Long bankAccountId,
    		@RequestParam LocalDate startDate, @RequestParam LocalDate endDate,Principal principal) {

        List<TransactionResponseDto> list =
                transactionService.getTransactionsByDateRange(
                        bankAccountId, startDate, endDate, principal);

        return ResponseEntity.ok(list);
    }
}
