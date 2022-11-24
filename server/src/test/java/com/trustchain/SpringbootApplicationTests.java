package com.trustchain;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trustchain.mapper.APIInvokeMapper;
import com.trustchain.service.MinioService;
import com.trustchain.model.*;
import com.trustchain.enums.HttpMethod;
import com.trustchain.enums.OrganizationType;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.fabric.FabricGateway;
import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.mapper.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class SpringbootApplicationTests {
    private static final Logger logger = LogManager.getLogger(SpringbootApplicationTests.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrganizationRegisterMapper registerOrganizationMapper;

    @Test
    void testAddUser() {
//        userMapper.insert(new User("test", DigestUtils.sha256Hex("test"), "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png", "e993ed9a-9119-45e6-8561-005e218e2005"));
//        userMapper.insert(new User("admin", DigestUtils.sha256Hex("admin"), "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png", "e7dc5bfe-14f6-49e5-8023-bb5000c26bda"));
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

        System.out.println(fg.query("queryOrganizationList"));
//        System.out.println(fg.query("queryAPIList"));
//        fg.invoke("createOrganization", "test", "test", "test", "test", "", "now");
//        UUID apiID = UUID.randomUUID();
//        System.out.println(apiID);
//        System.out.println(new Date().getTime());
//        System.out.println(LocalDateTime.now());
    }

    @Test
    void testCreateOrganization() {
        FabricGateway fg = new FabricGateway();

//        fg.invoke("createOrganization", "1583391160430190593", "数据资源可信共享运营平台", "EDUCATION", "", "", "2022-10-21 17:34:05");
//        fg.invoke("createOrganization", "e7dc5bfe-14f6-49e5-8023-bb5000c26bda", "admin", "管理", "", "", LocalDateTime.now().toString());
        System.out.println(fg.query("queryOrganizationList"));
    }

    @Test
    void testCreateAPI() {
        FabricGateway fg = new FabricGateway();

//        fg.invoke("createAPI", "", "", "", "", "", "", "", "", "", "", "", "", "");
        System.out.println(fg.query("queryAPIList"));
    }


    @Test
    void testUUID() {
        System.out.println(UUID.randomUUID());
    }

    @Autowired
    private OrganizationMapper organizationMapper;

    @Test
    void testRegisterOrganization() {
        Organization org = new Organization();
        org.setName("数据资源可信共享运营平台");
        org.setType(OrganizationType.EDUCATION);
        org.setTelephone("13915558435");
        org.setEmail("1843781563@qq.com");
        org.setCity("北京市,市辖区,海淀区");
        org.setAddress("学院路37号");
        org.setProvideNode(true);
        org.setNumNodes(4);
        org.setCreatedTime(new Date());

        organizationMapper.insert(org);
    }

    @Test
    void testBcrypto() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String pwd = encoder.encode("258667");
        System.out.println(pwd);
        System.out.println(encoder.matches("258667", pwd));
    }

    @Test
    void testFastJson() {
//        Fuck you = new Fuck(1, Gender.BOY);
//        String jsonString = JSON.toJSONString(you);
//        System.out.println(jsonString);    // {"gender":"BOY","id":1}

        Organization fuck = new Organization();

//        fuck.setCreatedTime(new Date());
//        fuck.setSuperior(null);
        Long id = fuck.getSuperior();

        JSONObject obj = JSONObject.parseObject(JSONObject.toJSONString(fuck));
        obj.put("good", id);

        System.out.println(obj);

//        fuck.setType(OrganizationType.EDUCATION);
//        System.out.println(fuck);
//        System.out.println(JSONObject.toJSONString(fuck, SerializerFeature.WriteEnumUsingToString));
//        System.out.println(JSONObject.toJSONString(fuck, SerializerFeature.WriteEnumUsingName));
//        System.out.println(JSON.toJSON(fuck).toString());
//        System.out.println(JSONObject.toJSONString(OrganizationType.EDUCATION));
//        Organization fuck = new Organization();
//
//        fuck.setType(OrganizationType.EDUCATION);
//        System.out.println(fuck);
//        System.out.println(OrganizationType.EDUCATION.name());
//        System.out.println(JSONObject.toJSONString(fuck, SerializerFeature.WriteEnumUsingToString));
//        System.out.println(JSONObject.toJSONString(fuck, SerializerFeature.WriteEnumUsingName));
//        System.out.println(JSON.toJSON(fuck).toString());
//        System.out.println(JSONObject.toJSONString(OrganizationType.EDUCATION));
    }

    @Test
    void testSaveFile() throws Exception {
        Resource res = new ClassPathResource("static");

        File dir = new File(res.getFile().getPath() + "/123456");
        if (dir.mkdir()) {
            System.out.println("Godd");
        }

        Long fuck = Long.parseLong("1234353452525");
        System.out.println("8432098/" + fuck + "/389283012");
    }

    @Test
    void testAddAmin() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("258667"));
        admin.setOrganization(Long.parseLong("1583391160430190593"));
        admin.setCreatedTime(new Date());

        userMapper.insert(admin);
    }

    @Test
    void testAPI() {
        APIRegister apiRegister = new APIRegister();
        apiRegister.setName("随便");
        apiRegister.setUrl("https://www.baidu.com");
        apiRegister.setMethod(HttpMethod.GET);
        apiRegister.setVersion("1.0.0");
        apiRegister.setStatus(RegisterStatus.PROCESSED);
        apiRegister.setApplyTime(new Date());

        System.out.println(JSONObject.toJSONString(apiRegister));
    }

    @Test
    void testJoin() {
        OrganizationInfo organizationInfo = organizationMapper.getOrganizationInformation(Long.parseLong("1583391160430190593"));

//        JSONObject organizationInfo = organizationMapper.getOrganizationInformation("1585171695289765890");

        System.out.println(organizationInfo);
        System.out.println(JSONObject.toJSONString(organizationInfo, SerializerFeature.WriteNullStringAsEmpty));

//        Organization org = organizationMapper.fuck("1583391160430190593");
//        System.out.println(org);
    }

    @Autowired
    private APIInvokeMapper apiInvokeMapper;

    @Test
    void testOrderByTime() {
        Long id = Long.parseLong("1585170615678930946");

        LambdaQueryWrapper<APIInvoke> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(APIInvoke::getApplyTime);

        List<APIInvokeApplyInfo> apiInvokeApplyList = apiInvokeMapper.getAPIInvokeApplyList(id, queryWrapper);

        System.out.println(apiInvokeApplyList);
    }

    @Autowired
    private MinioService minioService;

    @Test
    void testMinio() {
        try {
//            minioSerive.listBuckets();
//            minioService.copy("organization_register/1593230328973996033/logo.jpg",
//                    "organization/1593230328973996033/logo.jpg");
            minioService.createBucket("fuck");
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    void testCustomSerializer() {

        OrganizationRegister or = registerOrganizationMapper.selectById(Long.parseLong("1593230328973996033"));

        System.out.println(JSONObject.toJSONString(or));
    }
}

