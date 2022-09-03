package com.trustchain.sdkjava.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trustchain.sdkjava.SdkJavaApplication;
import com.trustchain.sdkjava.mapper.UserMapper;
import com.trustchain.sdkjava.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @PostMapping("/user/login")
    public ResponseEntity<Object> Login(@RequestBody LoginRequest login) {
        logger.info(login);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", login.getUsername()).eq("password", login.getPassword());
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }
    }

    @PostMapping("/user/logout")
    public ResponseEntity<Object> Logout(@RequestBody Object logout) {
        return ResponseEntity.status(HttpStatus.OK).body("Nothing");
    }
}

@Getter
@Setter
@ToString
class LoginRequest {
    private String username;
    private String password;
}