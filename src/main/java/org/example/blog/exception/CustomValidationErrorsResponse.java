package org.example.blog.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomValidationErrorsResponse extends CustomErrors {

    private final Map<String, String> errors;

    public CustomValidationErrorsResponse(int status, final Map<String, String> errors) {
        super(status);
        this.errors = errors;
    }

}
