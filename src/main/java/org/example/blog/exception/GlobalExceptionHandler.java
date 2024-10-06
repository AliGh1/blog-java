package org.example.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        CustomValidationErrorResponse response = new CustomValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CustomErrorResponse> handleResponseStatusExceptions(ResponseStatusException ex) {
        CustomErrorResponse response = new CustomErrorResponse(ex.getStatusCode().value(), ex.getMessage());

        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<CustomValidationErrorResponse> handleResponseStatusExceptions(CustomValidationException ex) {
        CustomValidationErrorResponse response = new CustomValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getErrors());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomErrorResponse> handleResponseStatusExceptions(AuthenticationException ex) {
        CustomErrorResponse response = new CustomErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
