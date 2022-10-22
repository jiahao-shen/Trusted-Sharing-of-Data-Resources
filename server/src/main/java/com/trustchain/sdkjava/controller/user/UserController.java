package com.trustchain.sdkjava.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.util.Date;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserMapper userMapper;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @PostMapping("/user/login")
    public ResponseEntity<Object> userLogin(@RequestBody JSONObject request, HttpSession session) {
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
    public ResponseEntity<Object> userLogout(HttpSession session) {
        session.setAttribute("user", null);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/user/register")
    public ResponseEntity<Object> userRegister(@RequestBody JSONObject request, HttpSession session) {
        User user = new User();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        user.setId(request.getString("id"));
        user.setUsername(request.getString("id"));
        user.setPassword(encoder.encode(request.getString("password")));
        user.setOrganization(Long.parseLong(request.getString("organization")));
        user.setCreatedTime(new Date());

        int count = userMapper.insert(user);
        if (count != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }

    @GetMapping("/user/list")
    public ResponseEntity<Object> userList(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id", "username", "created_time").eq("organization", user.getOrganization());
            List<User> userList = userMapper.selectList(queryWrapper);
            return ResponseEntity.status(HttpStatus.OK).body(userList);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }
    }

    @PostMapping("/user/exist")
    public ResponseEntity<Object> userExist(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        User user = userMapper.selectById(request.getString("id"));
        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }
}
