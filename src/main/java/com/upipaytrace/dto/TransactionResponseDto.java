package com.upipaytrace.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


import com.upipaytrace.enums.TransactionType;
import com.upipaytrace.enums.UpiAppSource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionResponseDto {
	
    private LocalDate transactionDate;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String referenceText;
    private UpiAppSource upiAppSource;
    private LocalDateTime createdAt;

}
