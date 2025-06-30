package com.bookstore.backend.exception;

import lombok.Data;

import java.util.Map;

@Data
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(int status, String message, Map<String, String> errors) {
        super(status, message);
        this.errors = errors;
    }
} 