package com.trustchain.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trustchain.enums.UserType;
import com.trustchain.mapper.UserMapper;
import com.trustchain.model.User;
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

    /**
     * 用户登录
     */
    @PostMapping("/user/login")
    public ResponseEntity<Object> userLogin(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, request.getString("username"));

        User user = userMapper.selectOne(queryWrapper);

        if (user != null && encoder.matches(request.getString("password"), user.getPassword())) {
            session.setAttribute("login", user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户名或密码错误");
        }
    }

    /**
     * 用户退出登录
     */
    @GetMapping("/user/logout")
    public ResponseEntity<Object> userLogout(HttpSession session) {
        session.setAttribute("login", null);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 用户注册
     */
    @PostMapping("/user/register")
    public ResponseEntity<Object> userRegister(@RequestBody JSONObject request, HttpSession session) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User();
        user.setUsername(request.getString("username"));
        user.setPassword(encoder.encode(request.getString("password")));
        user.setOrganization(Long.parseLong(request.getString("organization")));
        user.setType(UserType.valueOf(request.getString("type")));
        user.setCreatedTime(new Date());

        int count = userMapper.insert(user);
        if (count != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }

    /**
     * 获取指定机构下的用户列表
     */
    @GetMapping("/user/list")
    public ResponseEntity<Object> userList(HttpSession session) {
        User login = (User) session.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getId, User::getUsername, User::getType, User::getCreatedTime)
                .eq(User::getOrganization, login.getOrganization());

        List<User> userList = userMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    /**
     * 判断用户是否存在
     */
    @PostMapping("/user/exist")
    public ResponseEntity<Object> userExist(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, request.getString("username"));

        User user = userMapper.selectOne(queryWrapper);

        if (user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }
}
