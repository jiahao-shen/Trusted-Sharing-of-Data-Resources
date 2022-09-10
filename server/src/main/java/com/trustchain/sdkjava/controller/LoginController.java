package com.trustchain.sdkjava.controller;

import lombok.Data;
import com.trustchain.sdkjava.model.User;
import com.trustchain.sdkjava.mapper.UserMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @PostMapping("/user/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request, HttpSession session) {
        logger.info(request);

        User user = userMapper.selectById(request.getUsername());

        if (user != null && user.getPassword().equals(request.getPassword())) {
            session.setAttribute("user", user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }
    }

    @GetMapping("/user/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        session.setAttribute("user", null);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}

@Data
class LoginRequest {
    private String username;
    private String password;
}