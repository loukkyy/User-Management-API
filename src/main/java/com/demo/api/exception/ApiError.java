package com.demo.api.exception;

import lombok.Data;

@Data
public class ApiError {
	
	private int status;
	private String message;
	private String details;

	public ApiError(final int status, final String message, final String details) {
		this.status = status;
		this.message = message;
		this.details = details;
		
	}
}
