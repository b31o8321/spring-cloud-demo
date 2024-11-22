package com.example.user.exception;

import lombok.Getter;

@Getter
public class GlobalException extends Exception {
    private final String code;

    public GlobalException(String code, String message)
    {
        super(message);
        this.code = code;
    }
}
