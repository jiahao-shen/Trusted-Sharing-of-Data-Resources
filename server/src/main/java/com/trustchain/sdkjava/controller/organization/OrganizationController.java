package com.trustchain.sdkjava.controller.organization;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trustchain.sdkjava.mapper.OrganizationMapper;
import com.trustchain.sdkjava.mapper.OrganizationRegisterMapper;
import com.trustchain.sdkjava.model.Organization;
import com.trustchain.sdkjava.enums.OrganizationType;
import com.trustchain.sdkjava.model.OrganizationRegister;
import com.trustchain.sdkjava.enums.RegisterStatus;
import com.trustchain.sdkjava.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
public class OrganizationController {
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private OrganizationRegisterMapper organizationRegisterMapper;

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

    @PostMapping("/organization/register/request/reply")
    public ResponseEntity<Object> organizationRegisterRequsetReply(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));
        OrganizationRegister organizationRegister = organizationRegisterMapper.selectById(Long.parseLong(request.getString("serialNumber")));
        organizationRegister.setStatus(reply);
        if (reply == RegisterStatus.REJECT) {
            String reason = request.getString("reason");
            organizationRegister.setReplyMessage(reason);
        }
        organizationRegister.setReplyTime(new Date());

        int count = organizationRegisterMapper.updateById(organizationRegister);
        if (count != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(false);
        }
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
}