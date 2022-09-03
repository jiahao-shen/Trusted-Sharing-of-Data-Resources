package com.trustchain.sdkjava;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trustchain.sdkjava.mapper.UserMapper;
import com.trustchain.sdkjava.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SdkJavaApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Test
    void testMySQL() {
        System.out.println("Test MySQL");
    }

    @Test
    void testAddUser() {
        userMapper.insert(new User("plus", DigestUtils.sha256Hex("258667"), "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"));
    }

    @Test
    void testQueryUser() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("username").eq("username", "plus").eq("password", "792dd7a5054293e4e7efcb50b896bbce8f58426285608bd8fcbdaf430f413d30");
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

}
