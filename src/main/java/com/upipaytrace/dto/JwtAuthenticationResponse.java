package com.upipaytrace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtAuthenticationResponse {
	
	private String token;
}
