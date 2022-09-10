package com.trustchain.sdkjava;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trustchain.sdkjava.fabric.FabricGateway;
import com.trustchain.sdkjava.mapper.UserMapper;
import com.trustchain.sdkjava.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

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

    @Test
    void testFabric() {
        FabricGateway fg = new FabricGateway();

//        try {
//            JSONArray jsonArray = new JSONArray(fg.query("queryAPIList"));
//            for (int i = 0; i < jsonArray.length(); i++) {
//                System.out.println(jsonArray.getJSONObject(i).get("id"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        System.out.println(fg.query("queryAPIList"));
//        fg.invoke("createOrganization", "test", "test", "test", "test", "", "now");
//        System.out.println(fg.query("queryOrganizationList"));
//        UUID apiID = UUID.randomUUID();
//        System.out.println(apiID);
//        System.out.println(new Date().getTime());
//        System.out.println(LocalDateTime.now());
    }

}
