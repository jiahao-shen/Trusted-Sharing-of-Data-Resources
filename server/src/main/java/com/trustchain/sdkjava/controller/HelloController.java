package com.trustchain.sdkjava.controller;

import com.trustchain.sdkjava.mapper.UserMapper;
import com.trustchain.sdkjava.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {
    private UserMapper userMapper;
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/test")
    public String test() {
//        userMapper.insert(new User("plus", "258667", "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"));
        List<User> userList = userMapper.selectList(null);
        return "Test Page";
    }
}
