package com.demo.common.service.user.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.demo.common.service.user.mapper.UserMapper;
import com.demo.common.service.user.model.dto.AuthDTO;
import com.demo.common.service.user.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Snowflake snowflake;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void create(AuthDTO authDTO) {
        authDTO.setId(snowflake.nextId());
        authDTO.setPassword(passwordEncoder.encode(authDTO.getPassword()));
        authDTO.setCreatedAt(System.currentTimeMillis());
        authDTO.setUpdatedAt(System.currentTimeMillis());
        authDTO.setDeletedAt(0L);

        userMapper.create(authDTO);
    }

    public UserDTO findById(Long id) {
        if (redisTemplate.hasKey("user:" + id)) {
            return (UserDTO) redisTemplate.opsForValue().get("user:" + id);
        } else {
            UserDTO user = userMapper.findById(id);
            redisTemplate.opsForValue().set("user:" + id, user, 60, TimeUnit.SECONDS);
            return user;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.findByUsername(username);
    }
}
