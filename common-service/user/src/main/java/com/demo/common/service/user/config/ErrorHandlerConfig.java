package com.demo.common.service.user.config;

import com.demo.common.service.user.exception.GlobalException;
import com.demo.common.service.user.model.vo.ResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerConfig {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseVO<String>> handleException(Exception e)
    {
        if (e instanceof GlobalException) {
            String businessErrorCode = ((GlobalException) e).getCode();
            int httpErrorCode = Integer.parseInt(businessErrorCode.substring(0, 3));
            ResponseVO<String> responseVO = new ResponseVO<>(businessErrorCode, e.getMessage(), null);

            return new ResponseEntity<>(responseVO, HttpStatusCode.valueOf(httpErrorCode));
        }

        // undefined exception
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
