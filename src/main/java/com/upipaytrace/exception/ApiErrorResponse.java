package com.upipaytrace.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {

	private String message;
	private int status;
	private LocalDateTime timestamp;
}
