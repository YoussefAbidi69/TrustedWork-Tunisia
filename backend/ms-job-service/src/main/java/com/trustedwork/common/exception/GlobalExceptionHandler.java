package com.trustedwork.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
		return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI(), null);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<Map<String, Object>> handleConflict(ConflictException ex, HttpServletRequest request) {
		return buildError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI(), null);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
		return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<Map<String, String>> fieldErrors = ex.getBindingResult()
				.getAllErrors()
				.stream()
				.filter(FieldError.class::isInstance)
				.map(FieldError.class::cast)
				.map(fe -> {
					Map<String, String> m = new LinkedHashMap<>();
					m.put("field", fe.getField());
					m.put("message", fe.getDefaultMessage());
					return m;
				})
				.toList();

		return buildError(HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(), fieldErrors);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleOther(Exception ex, HttpServletRequest request) {
		return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getRequestURI(), null);
	}

	private ResponseEntity<Map<String, Object>> buildError(HttpStatus status,
												 String message,
												 String path,
												 Object details) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());
		body.put("message", message);
		body.put("path", path);
		if (details != null) {
			body.put("errors", details);
		}
		return ResponseEntity.status(status).body(body);
	}
}
