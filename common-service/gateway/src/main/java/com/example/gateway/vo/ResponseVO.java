package com.example.gateway.vo;

import lombok.Data;

@Data
public class ResponseVO<T> {
    private String code;
    private String msg;
    private T data;
}
