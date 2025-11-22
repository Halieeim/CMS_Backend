package com.clinic.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, Object> buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("message", message);
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));
        return errorDetails;
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleIoException(IOException ex, WebRequest request) {
        logger.error("IOException occurred", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgException(IllegalArgumentException ex, WebRequest request) {
        logger.error("IllegalArgumentException occurred", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request));
    }

    @ExceptionHandler(NotAssociatedToEntityException.class)
    public ResponseEntity<Map<String, Object>> handleNotAssociatedToEntityException(NotAssociatedToEntityException ex, WebRequest request) {
        logger.error("NotAssociatedToEntityException occurred", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request));
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<Map<String, Object>> handleNotAuthenticated(NotAuthenticatedException ex, WebRequest request) {
        logger.error("NotAuthenticatedException occurred", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex, WebRequest request) {
        logger.error("NotFoundException occurred", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        logger.error("AccessDeniedException occurred", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildErrorResponse("Access denied: " + ex.getMessage(), HttpStatus.FORBIDDEN, request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        logger.error("Validation error occurred", ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> response = buildErrorResponse("Validation failed", HttpStatus.BAD_REQUEST, request);
        response.put("validationErrors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEntityAlreadyExists(EntityExistsException ex, WebRequest request) {
        logger.error("EntityAlreadyExistsException occurred", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildErrorResponse("Entity Already Exists: " + ex.getMessage(), HttpStatus.BAD_REQUEST, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred", ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR, request));
    }
}