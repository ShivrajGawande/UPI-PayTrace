package com.upipaytrace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginDto {
	
	private String email;
	private String password;

}
