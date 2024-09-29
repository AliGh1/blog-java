package org.example.blog.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
public class CustomValidationErrorsResponse {
    private final int status;
    private final Map<String, String> errors;
    private final String timestamp;


    public CustomValidationErrorsResponse(int statusCode, Map<String, String> errors) {
        this.status = statusCode;
        this.errors = errors;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
