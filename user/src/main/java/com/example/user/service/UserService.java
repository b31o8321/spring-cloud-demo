package com.example.user.service;

import cn.hutool.core.lang.Snowflake;
import com.example.user.mapper.UserMapper;
import com.example.user.model.dto.AuthDTO;
import com.example.user.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void create(AuthDTO authDTO) {
        authDTO.setId(snowflake.nextId());
        authDTO.setPassword(passwordEncoder.encode(authDTO.getPassword()));
        authDTO.setCreatedAt(System.currentTimeMillis());
        authDTO.setUpdatedAt(System.currentTimeMillis());
        authDTO.setDeletedAt(0L);

        userMapper.create(authDTO);
    }

    public UserDTO findById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.findByUsername(username);
    }
}
