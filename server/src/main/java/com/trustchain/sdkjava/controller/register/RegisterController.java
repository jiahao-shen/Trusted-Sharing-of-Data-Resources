package com.trustchain.sdkjava.controller.register;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trustchain.sdkjava.mapper.OrganizationMapper;
import com.trustchain.sdkjava.mapper.RegisterOrganizationMapper;
import com.trustchain.sdkjava.model.Organization;
import com.trustchain.sdkjava.model.OrganizationType;
import com.trustchain.sdkjava.model.RegisterOrganization;
import com.trustchain.sdkjava.model.RegisterStatus;
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
public class RegisterController {
    @Autowired
    private RegisterOrganizationMapper registerOrganizationMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    private static final Logger logger = LogManager.getLogger(RegisterController.class);

    @PostMapping("/register/organization")
    public ResponseEntity<Object> registerOrganization(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        RegisterOrganization registerOrganization = new RegisterOrganization();
        registerOrganization.setName(request.getString("name"));
        registerOrganization.setLogo(request.getString("logo"));
        registerOrganization.setType(OrganizationType.valueOf(request.getString("type")));
        registerOrganization.setTelephone(request.getString("telephone"));
        registerOrganization.setEmail(request.getString("email"));
        registerOrganization.setCity(request.getString("city"));
        registerOrganization.setAddress(request.getString("address"));
        registerOrganization.setIntroduction(request.getString("introduction"));
        registerOrganization.setSuperior(request.getLong("superior"));
        registerOrganization.setProvideNode(request.getBoolean("provideNode"));
        registerOrganization.setNumNodes(request.getInteger("numNodes"));
        registerOrganization.setFile(request.getString("file"));
        registerOrganization.setStatus(RegisterStatus.PROCESSED);
        registerOrganization.setApplyTime(new Date());

        int count = registerOrganizationMapper.insert(registerOrganization);

        if (count != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(registerOrganization.getSerialNumber());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知错误");
        }
    }

    @GetMapping("register/organization/list")
    public ResponseEntity<Object> getOrganizationList() {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name");
        List<Organization> organizationList = organizationMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(organizationList);
    }
}
