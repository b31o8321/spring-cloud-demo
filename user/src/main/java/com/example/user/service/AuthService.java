package com.example.user.service;

import com.example.user.mapper.AuthMapper;
import com.example.user.model.dto.AuthDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private AuthMapper authMapper;

    public AuthDTO findById(Long id) {
        return authMapper.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authMapper.findByUsername(username);
    }
}
