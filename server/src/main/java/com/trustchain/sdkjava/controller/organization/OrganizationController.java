package com.trustchain.sdkjava.controller.organization;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.trustchain.sdkjava.mapper.OrganizationMapper;
import com.trustchain.sdkjava.mapper.OrganizationRegisterMapper;
import com.trustchain.sdkjava.model.Organization;
import com.trustchain.sdkjava.enums.OrganizationType;
import com.trustchain.sdkjava.model.OrganizationRegister;
import com.trustchain.sdkjava.enums.RegisterStatus;
import com.trustchain.sdkjava.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
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

    private static final Logger logger = LogManager.getLogger(OrganizationController.class);

    /**
     * 机构注册申请
     */
    @PostMapping("/organization/register/apply")
    public ResponseEntity<Object> organizationRegisterApply(@RequestPart("logo") MultipartFile logo, @RequestPart("info") JSONObject request, @RequestPart("file") MultipartFile file, HttpSession session) {

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

                // TODO: 文件读写
//                Resource resource = new ClassPathResource("static");
//                String directory = resource.getFile().getPath() + "/" + serialNumber;
//                Files.createDirectory(Paths.get(directory)); // 按申请号创建文件夹
//
//                // 写入Logo和File
//                logo.transferTo(new File(directory + "/logo.jpg"));
//                file.transferTo(new File(directory + "/file.zip"));
//
//                // 更新数据库中的Logo和File
//                organizationRegister.setLogo("http://localhost:5173/server/" + serialNumber + "/logo.jpg");
//                organizationRegister.setFile("http://localhost:5173/server/" + serialNumber + "/file.zip");
//
//                organizationRegisterMapper.updateById(organizationRegister);

                return ResponseEntity.status(HttpStatus.OK).body(serialNumber);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知错误");
        }
    }

    /**
     * 机构注册申请列表
     */
    @GetMapping("/organization/register/apply/list")
    public ResponseEntity<Object> organizationRegisterApplyList(HttpSession session) {
        User login = (User) session.getAttribute("user");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }

        QueryWrapper<OrganizationRegister> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("superior", login.getOrganization()).orderByDesc("apply_time");
        List<OrganizationRegister> organizationRegisterList = organizationRegisterMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterList);
    }

    /**
     * 查询注册申请进度
     */
    @PostMapping("/organization/register/apply/progress")
    public ResponseEntity<Object> organizationRegisterApplyProgress(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        ArrayList<Long> serialNumbers = request.getObject("serialNumbers", ArrayList.class);

        List<OrganizationRegister> organizationRegisterList = organizationRegisterMapper.selectBatchIds(serialNumbers);

        return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterList);
    }

    /**
     * 回复注册申请
     */
    @PostMapping("/organization/register/reply")
    public ResponseEntity<Object> organizationRegisterReply(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));

        OrganizationRegister organizationRegister = organizationRegisterMapper.selectById(Long.parseLong(request.getString("serialNumber")));

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
        organizationRegister.setStatus(reply);  // 更新注册状态
        if (reply == RegisterStatus.REJECT) {
            String reason = request.getString("reason");
            organizationRegister.setReplyMessage(reason);   // 更新回复理由
        }
        organizationRegister.setReplyTime(new Date());  // 更新回复时间
        organizationRegisterMapper.updateById(organizationRegister);

        // TODO: 文件读写
//        try {
//            String serialNumber = organizationRegister.getSerialNumber().toString();
//            Resource resource = new ClassPathResource("static");
//            String oldDirectory = resource.getFile().getPath() + "/" + serialNumber;
//            String newDirectory = resource.getFile().getPath() + "/" + orgID;
//            // 移动Logo和File至新目录
//            new File(oldDirectory).renameTo(new File(newDirectory));
//        } catch (Exception e) {
//            logger.warn("移动文件失败");
//        }

        // 更新机构
//        organization.setLogo("http://localhost:5173/server/" + orgID + "/logo.jpg");
//        organization.setFile("http://localhost:5173/server/" + orgID + "/file.zip");
//        organizationMapper.updateById(organization);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    /**
     * 判断机构是否存在
     */
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

    /**
     * 获得全部机构用于选择
     */
    @GetMapping("/organization/selectList")
    public ResponseEntity<Object> organizationSelectList() {
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name");
        List<Organization> organizationList = organizationMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(organizationList);
    }

    /**
     * 获取指定机构的信息
     */
    @PostMapping("/organization/information")
    public ResponseEntity<Object> organizationInformation(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        @Data
        @EqualsAndHashCode(callSuper = true)
        class OrganizationInfo extends Organization {
            private String superiorName;
        }

        OrganizationInfo organizationInfo = organizationMapper.selectJoinOne(OrganizationInfo.class,
                new MPJLambdaWrapper<Organization>()
                        .eq(Organization::getId, Long.parseLong("1583391160430190593"))
                        .selectAll(Organization.class)
                        .selectAs(Organization::getName, OrganizationInfo::getSuperiorName)
                        .leftJoin(Organization.class, Organization::getId, Organization::getSuperior));

        System.out.println(organizationInfo);
//        Organization organization = organizationMapper.selectById(request.getLong("id"));
//
//        JSONObject response = JSONObject.parseObject(JSONObject.toJSONString(organization));
//
//        Organization superior = organizationMapper.selectById(organization.getSuperior());
//
//        if (superior != null) {
//            response.put("superior", superior.getName());
//        } else {
//            response.put("superior", "");
//        }

        return ResponseEntity.status(HttpStatus.OK).body(organizationInfo);
    }

    @GetMapping("/organization/subordinate/list")
    public ResponseEntity<Object> organizationSubordinateList(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }

        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("superior", user.getOrganization());
        List<Organization> subordinateList = organizationMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(subordinateList);
    }
}
