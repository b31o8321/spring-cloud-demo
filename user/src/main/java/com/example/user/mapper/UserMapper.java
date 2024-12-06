package com.example.user.mapper;

import com.example.user.model.dto.AuthDTO;
import com.example.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserDTO findById(Long id);

    AuthDTO findByUsername(String username);

    void create(AuthDTO authDTO);
}
