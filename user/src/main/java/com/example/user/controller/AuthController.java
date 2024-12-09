package com.example.user.controller;

import com.example.user.model.dto.AuthDTO;
import com.example.user.model.vo.ResponseVO;
import com.example.user.service.impl.AuthServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.example.user.ValidateTokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/user-center/auth")
public class AuthController {
    @Autowired
    private AuthServiceImpl authServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<ResponseVO<Object>> login(@RequestBody AuthDTO authDTO, HttpServletResponse response) {
        String jwt = authServiceImpl.login(authDTO);

        if (StringUtils.isEmpty(jwt)) {
            return new ResponseEntity<>(ResponseVO.error("login failed!"), HttpStatus.UNAUTHORIZED);
        }
        response.addHeader("Authorization", "Bearer " + jwt);
        return ResponseEntity.ok(ResponseVO.success());
    }

    @PostMapping("/validate")
    public ResponseEntity<ResponseVO<String>> validate(@RequestBody ValidateTokenVO validateTokenVO) {
        String newToken = authServiceImpl.validate(validateTokenVO);

        if (StringUtils.isEmpty(newToken)) {
            return new ResponseEntity<>(ResponseVO.error("token invalid!"), HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(ResponseVO.success(newToken), HttpStatus.OK);
    }

    @PostMapping("/update-jwt")
    public ResponseEntity<String> updateJwt(@RequestBody String jwt) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey("secret").build();
        Claims claims = parser.parseClaimsJws(jwt).getBody();
        Claims newClaims = Jwts.claims();

        // 可以根据查询到的用户信息，重新设置claims
        newClaims.putAll(claims);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + 1000 * 60 * 60 * 24);
        newClaims.put("exp", expirationDate);
        newClaims.setExpiration(expirationDate);

        String newJwt = Jwts.builder()
                .setClaims(newClaims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor("secret".getBytes()))
                .compact();

        return new ResponseEntity<>(newJwt, HttpStatus.OK);
    }
}
