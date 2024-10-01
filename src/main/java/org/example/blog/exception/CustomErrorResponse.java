package org.example.blog.exception;

import lombok.Getter;

@Getter
public class CustomErrorResponse extends CustomError {
    private final String message;

    public CustomErrorResponse(int status, String message) {
        super(status);
        this.message = message;
    }
}
