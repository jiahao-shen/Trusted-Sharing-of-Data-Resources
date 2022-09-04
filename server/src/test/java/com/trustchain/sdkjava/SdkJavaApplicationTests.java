package com.trustchain.sdkjava;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trustchain.sdkjava.mapper.UserMapper;
import com.trustchain.sdkjava.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SdkJavaApplicationTests {
    private static final Logger logger = LogManager.getLogger(SdkJavaApplicationTests.class);

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
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("username").eq("username", "plus").eq("password", "792dd7a5054293e4e7efcb50b896bbce8f58426285608bd8fcbdaf430f413d30");
//        User user = userMapper.selectOne(queryWrapper);
        long startTime = System.nanoTime();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "plus");

        for (int i = 0; i < 1000; i++) {
//            User user = userMapper.selectById("plus"); // 1137355770
                                                         // 1945233024

            User user = userMapper.selectOne(queryWrapper);
        }

        long endTime = System.nanoTime();

//        logger.info(user);
        logger.error(endTime - startTime);
    }

}
