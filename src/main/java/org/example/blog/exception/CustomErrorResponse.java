package org.example.blog.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CustomErrorResponse {
    private final int status;
    private final String message;
    private final String timestamp;


    public CustomErrorResponse(int statusCode, String message) {
        this.status = statusCode;
        this.message = message;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
