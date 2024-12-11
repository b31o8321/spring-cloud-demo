package com.demo.common.service.user.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.demo.common.service.user.model.dto.AuthDTO;
import com.demo.internal.service.user.AuthService;
import com.demo.internal.service.user.ValidateTokenVO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.security.Keys;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@DubboService
public class AuthServiceImpl implements AuthService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private Snowflake snowflake;

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public Authentication authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public String issuerJwt(Authentication authentication) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpiration);
        return Jwts.builder()
                .setSubject(((UserDetails) authentication.getPrincipal()).getUsername())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public String validate(ValidateTokenVO validateTokenVO) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build();

        try {
            Jwt jwt = parser.parse(validateTokenVO.getToken());
            DefaultClaims claims = (DefaultClaims) jwt.getBody();

            return buildJWT(claims.getSubject());
        } catch (ExpiredJwtException e) {
            // JWT已过期
            throw e;
        } catch (MalformedJwtException e) {
            // JWT格式不正确
            throw e;
        } catch (UnsupportedJwtException e) {
            // 不支持的JWT类型
            throw e;
        } catch (Exception e) {
            // 其他未知错误
            throw e;
        }
    }

    public String login(AuthDTO authDTO) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return buildJWT(authDTO.getUsername());
    }

    public String buildJWT(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
