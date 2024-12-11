package com.demo.common.service.user.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseVO<T> {
    private final String code;
    private final String message;
    private final T data;


    public static <T> ResponseVO<T> success() {
        return new ResponseVO<>("200000", "success", null);
    }
    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>("200000", "success", data);
    }

    public static <T> ResponseVO<T> error() {
        return new ResponseVO<>("400000", "fail", null);
    }
    public static <T> ResponseVO<T> error(String msg) {
        return new ResponseVO<>("400000", msg, null);
    }
}
