package org.example.blog.exception;

import lombok.Getter;

@Getter
public class CustomErrorResponse extends CustomErrors {
    private final String message;

    public CustomErrorResponse(int status, String message) {
        super(status);
        this.message = message;
    }
}
