package org.example.blog.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
public class CustomErrors {
    private final int status;
    private final String timestamp;
    private String message;
    private Map<String, String> errors;

    public CustomErrors(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public CustomErrors(int status, Map<String, String> errors) {
        this.status = status;
        this.errors = errors;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
