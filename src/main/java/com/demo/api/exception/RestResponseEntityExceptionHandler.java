package com.demo.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@ControllerAdvice
@ApiIgnore
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	public RestResponseEntityExceptionHandler() {
		super();
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.debug("handleHttpMessageNotReadable exception thrown");
		return super.handleExceptionInternal(ex, message(HttpStatus.BAD_REQUEST, ex), headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.debug("handleMethodArgumentNotValid exception thrown");
		return super.handleExceptionInternal(ex, message(HttpStatus.BAD_REQUEST, ex), headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler({ JwtException.class })
	protected ResponseEntity<Object> expiredJwtException(
		      RuntimeException ex, WebRequest request) {
		log.debug("JwtException exception thrown");
		return handleExceptionInternal(ex, message(HttpStatus.FORBIDDEN, ex), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}
	
	private ApiError message(final HttpStatus httpStatus, final Exception ex) {
		final String message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
		final String details = ex.getClass().getSimpleName();
		
		return new ApiError(httpStatus.value(), message, details);
	}
}
