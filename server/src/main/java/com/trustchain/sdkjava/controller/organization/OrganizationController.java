package com.trustchain.sdkjava.controller.organization;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trustchain.sdkjava.mapper.OrganizationMapper;
import com.trustchain.sdkjava.mapper.OrganizationRegisterMapper;
import com.trustchain.sdkjava.mapper.UserMapper;
import com.trustchain.sdkjava.model.Organization;
import com.trustchain.sdkjava.enums.OrganizationType;
import com.trustchain.sdkjava.model.OrganizationRegister;
import com.trustchain.sdkjava.enums.RegisterStatus;
import com.trustchain.sdkjava.model.User;
import com.trustchain.sdkjava.util.Generator;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class OrganizationController {
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private OrganizationRegisterMapper organizationRegisterMapper;

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LogManager.getLogger(OrganizationController.class);

    @PostMapping("/organization/register/request")
    public ResponseEntity<Object> organizationRegisterRequest(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        OrganizationRegister organizationRegister = new OrganizationRegister();
        organizationRegister.setName(request.getString("name"));
        organizationRegister.setLogo(request.getString("logo"));
        organizationRegister.setType(OrganizationType.valueOf(request.getString("type")));
        organizationRegister.setTelephone(request.getString("telephone"));
        organizationRegister.setEmail(request.getString("email"));
        organizationRegister.setCity(request.getString("city"));
        organizationRegister.setAddress(request.getString("address"));
        organizationRegister.setIntroduction(request.getString("introduction"));
        organizationRegister.setSuperior(Long.parseLong(request.getString("superior")));
        organizationRegister.setProvideNode(request.getBoolean("provideNode"));
        organizationRegister.setNumNodes(request.getInteger("numNodes"));
        organizationRegister.setFile(request.getString("file"));
        organizationRegister.setStatus(RegisterStatus.PROCESSED);
        organizationRegister.setApplyTime(new Date());

        int count = organizationRegisterMapper.insert(organizationRegister);

        if (count != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(organizationRegister.getSerialNumber());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知错误");
        }
    }

    @GetMapping("/organization/register/request/list")
    public ResponseEntity<Object> organizationRegisterRequestList(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            QueryWrapper<OrganizationRegister> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("superior", user.getOrganization());
            List<OrganizationRegister> organizationRegisterList = organizationRegisterMapper.selectList(queryWrapper);

            return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterList);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }
    }

    @PostMapping("/organization/register/request/progress")
    public ResponseEntity<Object> organizationRegisterRequestProgress(@RequestBody JSONObject request, HttpSession session) {
        try {
//            Long serialNumber = Long.parseLong(request.getString("serialNumber"));
//
//            OrganizationRegister organizationRegister = organizationRegisterMapper.selectById(serialNumber);
//            if (organizationRegister == null) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("申请号不存在");
//            }
//
//            JSONObject response = new JSONObject();
//            response.put("registerList", organizationRegister);
            logger.info(request);
            ArrayList<String> serialNumbers = request.getObject("serialNumbers", ArrayList.class);
//            ArrayList<Long> tmp =
            return ResponseEntity.status(HttpStatus.OK).body("Good");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @PostMapping("/organization/register/request/reply")
    public ResponseEntity<Object> organizationRegisterRequsetReply(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));

        OrganizationRegister organizationRegister = organizationRegisterMapper.selectById(Long.parseLong(request.getString("serialNumber")));
        organizationRegister.setStatus(reply);  // 更新注册状态
        if (reply == RegisterStatus.REJECT) {
            String reason = request.getString("reason");
            organizationRegister.setReplyMessage(reason);   // 更新回复理由
        }
        organizationRegister.setReplyTime(new Date());  // 更新回复时间

        // 创建机构
        Organization organization = new Organization();
        organization.setName(organizationRegister.getName());
        organization.setLogo(organizationRegister.getLogo());
        organization.setType(organizationRegister.getType());
        organization.setTelephone(organizationRegister.getTelephone());
        organization.setEmail(organizationRegister.getEmail());
        organization.setCity(organizationRegister.getCity());
        organization.setAddress(organizationRegister.getAddress());
        organization.setIntroduction(organizationRegister.getIntroduction());
        organization.setSuperior(organizationRegister.getSuperior());
        organization.setProvideNode(organizationRegister.isProvideNode());
        organization.setNumNodes(organizationRegister.getNumNodes());
        organization.setFile(organizationRegister.getFile());
        organization.setCreatedTime(new Date());

        int count = organizationMapper.insert(organization);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("机构创建失败");
        }

        organizationRegister.setId(organization.getId());   // 机构注册绑定机构ID

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User root = new User();
        root.setId(Generator.userID(8));
        root.setUsername("管理员");
        root.setPassword(encoder.encode(Generator.password(12)));
        root.setOrganization(organization.getId());
        root.setCreatedTime(new Date());

        count = userMapper.insert(root);

        if (count == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("管理员创建失败");
        }

        count = organizationRegisterMapper.updateById(organizationRegister);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失败");
        }

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PostMapping("/organization/exist")
    public ResponseEntity<Object> organizationExist(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        String name = request.getString("name");
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);

        Organization organization = organizationMapper.selectOne(queryWrapper);
        if (organization != null) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
    }

    @GetMapping("/organization/list")
    public ResponseEntity<Object> organizationList() {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name");
        List<Organization> organizationList = organizationMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(organizationList);
    }

    @PostMapping("/organization/information")
    public ResponseEntity<Object> organizationInformation(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        Organization organization = organizationMapper.selectById(request.getLong("id"));

        return ResponseEntity.status(HttpStatus.OK).body(organization);
    }
}
