package org.example.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ValidateTokenVO implements Serializable {
    private String path;
    private String token;
}