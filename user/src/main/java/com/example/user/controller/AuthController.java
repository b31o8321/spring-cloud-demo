package com.example.user.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @RequestMapping("/validate-jwt")
    public Boolean validateJwt(@RequestBody String jwt) {

    }
}
