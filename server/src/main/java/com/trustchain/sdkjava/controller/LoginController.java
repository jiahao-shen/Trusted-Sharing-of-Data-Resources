package com.trustchain.sdkjava.controller;

import com.trustchain.sdkjava.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @PostMapping(value = "/user/login")
    public ResponseEntity<Object> Login(@RequestBody LoginRequest login) {
        System.out.println(login);
        // TODO: 连接数据库
        if (login.getUsername().equals("test") && login.getPassword().equals("test")) {
            User user = new User();
            user.setUsername("test");
            user.setImageURL("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png");
            return ResponseEntity.status(200).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户名或密码错误");
        }
    }
}

@Getter
@Setter
@ToString
class LoginRequest {
    private String username;
    private String password;
}