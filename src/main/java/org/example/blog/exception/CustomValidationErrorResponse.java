package org.example.blog.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomValidationErrorResponse extends CustomError {
    private final Map<String, String> errors;

    public CustomValidationErrorResponse(int status, final Map<String, String> errors) {
        super(status);
        this.errors = errors;
    }
}
