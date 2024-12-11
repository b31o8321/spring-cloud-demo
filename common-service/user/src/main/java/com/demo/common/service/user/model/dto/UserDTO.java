package com.demo.common.service.user.model.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private Long createdAt;
    private Long updatedAt;
    private Long deletedAt;
}
