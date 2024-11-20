package com.example.user.model.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String accountName;
    private String nickName;
    private String email;
}
