package com.upipaytrace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegisterRequestDto {
	
	private String fullName;
	private String email;
	private String password;
}
