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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public ResponseEntity<Object> organizationRegisterRequest(@RequestPart("logo") MultipartFile logo, @RequestPart("info") JSONObject request, @RequestPart("file") MultipartFile file, HttpSession session) {

        // 新注册申请
        OrganizationRegister organizationRegister = new OrganizationRegister();
        organizationRegister.setName(request.getString("name"));
        organizationRegister.setType(OrganizationType.valueOf(request.getString("type")));
        organizationRegister.setTelephone(request.getString("telephone"));
        organizationRegister.setEmail(request.getString("email"));
        organizationRegister.setCity(request.getString("city"));
        organizationRegister.setAddress(request.getString("address"));
        organizationRegister.setIntroduction(request.getString("introduction"));
        organizationRegister.setSuperior(Long.parseLong(request.getString("superior")));
        organizationRegister.setProvideNode(request.getBoolean("provideNode"));
        organizationRegister.setNumNodes(request.getInteger("numNodes"));
        organizationRegister.setStatus(RegisterStatus.PROCESSED);
        organizationRegister.setApplyTime(new Date());

        int count = organizationRegisterMapper.insert(organizationRegister);

        if (count != 0) {
            try {
                String serialNumber = organizationRegister.getSerialNumber().toString();
                Resource resource = new ClassPathResource("static");
                String directory = resource.getFile().getPath() + "/" + serialNumber;
                Files.createDirectory(Paths.get(directory)); // 按申请号创建文件夹

                // 写入Logo和File
                logo.transferTo(new File(directory + "/logo.jpg"));
                file.transferTo(new File(directory + "/file.zip"));

                // 更新数据库中的Logo和File
                organizationRegister.setLogo("http://localhost:5173/server/" + serialNumber + "/logo.jpg");
                organizationRegister.setFile("http://localhost:5173/server/" + serialNumber + "/file.zip");

                organizationRegisterMapper.updateById(organizationRegister);

                return ResponseEntity.status(HttpStatus.OK).body(serialNumber);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知错误");
        }
    }

    @GetMapping("/organization/register/request/list")
    public ResponseEntity<Object> organizationRegisterRequestList(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            QueryWrapper<OrganizationRegister> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("superior", user.getOrganization()).orderByDesc("apply_time");
            List<OrganizationRegister> organizationRegisterList = organizationRegisterMapper.selectList(queryWrapper);

            return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterList);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }
    }

    @PostMapping("/organization/register/request/progress")
    public ResponseEntity<Object> organizationRegisterRequestProgress(@RequestBody JSONObject request, HttpSession session) {
        try {
            logger.info(request);
            ArrayList<Long> serialNumbers = request.getObject("serialNumbers", ArrayList.class);

            List<OrganizationRegister> organizationRegisterList = organizationRegisterMapper.selectBatchIds(serialNumbers);

            return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterList);
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
        organization.setType(organizationRegister.getType());
        organization.setTelephone(organizationRegister.getTelephone());
        organization.setEmail(organizationRegister.getEmail());
        organization.setCity(organizationRegister.getCity());
        organization.setAddress(organizationRegister.getAddress());
        organization.setIntroduction(organizationRegister.getIntroduction());
        organization.setSuperior(organizationRegister.getSuperior());
        organization.setProvideNode(organizationRegister.isProvideNode());
        organization.setNumNodes(organizationRegister.getNumNodes());
        organization.setCreatedTime(new Date());

        int count = organizationMapper.insert(organization);

        if (count == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("机构创建失败");
        }

        Long orgID = organization.getId();
        organizationRegister.setId(orgID);   // 机构注册绑定机构ID
        organizationRegisterMapper.updateById(organizationRegister);

        try {
            String serialNumber = organizationRegister.getSerialNumber().toString();
            Resource resource = new ClassPathResource("static");
            String oldDirectory = resource.getFile().getPath() + "/" + serialNumber;
            String newDirectory = resource.getFile().getPath() + "/" + orgID;
            // 移动Logo和File至新目录
            new File(oldDirectory).renameTo(new File(newDirectory));
        } catch (Exception e) {
            logger.warn("移动文件失败");
        }

        // 更新机构
        organization.setLogo("http://localhost:5173/server/" + orgID + "/logo.jpg");
        organization.setFile("http://localhost:5173/server/" + orgID + "/file.zip");
        organizationMapper.updateById(organization);

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

    @GetMapping("/organization/selectList")
    public ResponseEntity<Object> organizationSelectList() {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name");
        List<Organization> organizationList = organizationMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(organizationList);
    }

    @PostMapping("/organization/information")
    public ResponseEntity<Object> organizationInformation(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        Organization organization = organizationMapper.selectById(request.getLong("id"));

        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(organization));

        Organization superior = organizationMapper.selectById(organization.getSuperior());

        if (superior != null) {
            response.put("superior", superior.getName());
        } else {
            response.put("superior", "");
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
