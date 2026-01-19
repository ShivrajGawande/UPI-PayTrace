package com.upipaytrace.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.upipaytrace.service.StatementService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/statements")
public class StatementController {

    private final StatementService statementService;

    @PostMapping("/upload/{bankAccountId}")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file,
                                            @PathVariable Long bankAccountId,
                                            Principal principal) {

        if (file.isEmpty())
            return ResponseEntity.badRequest().body("CSV file is empty");

        if (file.getOriginalFilename() == null ||
            !file.getOriginalFilename().endsWith(".csv"))
            return ResponseEntity.badRequest().body("Please upload a CSV file");

        String response = statementService.processCsv(file, bankAccountId, principal);
        return ResponseEntity.ok(response);
    }
}
