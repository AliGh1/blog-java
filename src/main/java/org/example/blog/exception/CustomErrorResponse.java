package org.example.blog.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public record CustomErrorResponse(int status, String message, String timestamp) {
    public CustomErrorResponse(int status, String message) {
        this(status, message, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
    }

}
