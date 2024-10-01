package org.example.blog.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
abstract class CustomError {
    private final int status;
    private final String timestamp;

    public CustomError(int statusCode) {
        this.status = statusCode;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
