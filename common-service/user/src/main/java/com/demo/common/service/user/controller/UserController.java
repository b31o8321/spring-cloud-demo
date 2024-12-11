package com.demo.common.service.user.controller;

import com.demo.common.service.user.model.dto.AuthDTO;
import com.demo.common.service.user.model.dto.UserDTO;
import com.demo.common.service.user.model.vo.ResponseVO;
import com.demo.common.service.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-center/users")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping()
    public ResponseEntity<String> register(@RequestBody AuthDTO authDTO) {
        userServiceImpl.create(authDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/{id}")
    public ResponseVO<UserDTO> findById(@PathVariable Long id) {
        UserDTO user = userServiceImpl.findById(id);
        return ResponseVO.success(user);
    }
}
