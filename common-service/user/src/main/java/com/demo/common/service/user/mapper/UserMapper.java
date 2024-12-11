package com.demo.common.service.user.mapper;

import com.demo.common.service.user.model.dto.AuthDTO;
import com.demo.common.service.user.model.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserDTO findById(Long id);

    AuthDTO findByUsername(String username);

    void create(AuthDTO authDTO);
}
