package com.example.user.service;

import com.example.user.mapper.UserMapper;
import com.example.user.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public UserDTO findById(Long id) {
        return userMapper.findById(id);
    }
}
