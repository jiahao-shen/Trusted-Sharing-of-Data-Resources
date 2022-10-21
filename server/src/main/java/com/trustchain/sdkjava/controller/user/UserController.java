package com.trustchain.sdkjava.controller.dashboard.api.user;

import com.alibaba.fastjson.JSONObject;
import com.trustchain.sdkjava.mapper.UserMapper;
import com.trustchain.sdkjava.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping("/user/login")
    public ResponseEntity<Object> login(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = userMapper.selectById(request.getString("id"));

        if (user != null && encoder.matches(request.getString("password"), user.getPassword())) {
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
