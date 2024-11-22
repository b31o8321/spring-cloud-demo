package com.example.gateway.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidateTokenVO {
    private String path;
    private String token;
}
