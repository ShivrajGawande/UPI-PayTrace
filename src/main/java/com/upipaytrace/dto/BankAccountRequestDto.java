package com.upipaytrace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankAccountRequestDto {
	
	 private String bankName;
	 private String maskedAccountNumber;

}
