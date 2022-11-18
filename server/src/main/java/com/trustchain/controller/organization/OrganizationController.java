package com.trustchain.controller.organization;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trustchain.mapper.OrganizationMapper;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.service.MinioService;
import com.trustchain.model.User;
import com.trustchain.model.Organization;
import com.trustchain.enums.OrganizationType;
import com.trustchain.model.OrganizationInfo;
import com.trustchain.model.OrganizationRegister;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.service.FabricService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private MinioService minioService;

    @Autowired
    private FabricService fabricService;

    private static final Logger logger = LogManager.getLogger(OrganizationController.class);

    /**
     * 机构注册申请
     */
    @PostMapping("/organization/register/apply")
    public ResponseEntity<Object> organizationRegisterApply(@RequestPart("logo") MultipartFile logo,
                                                            @RequestPart("info") JSONObject request,
                                                            @RequestPart("file") MultipartFile file, HttpSession session) {

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
            String serialNumber = organizationRegister.getSerialNumber().toString();
            String logoPath = String.format("organization_register/%s/logo.jpg", serialNumber);
            String filePath = String.format("organization_register/%s/file.zip", serialNumber);
            // 上传至云盘
            minioService.upload(logo, logoPath);
            minioService.upload(file, filePath);
            // 更新Logo和File路径
            organizationRegister.setLogo(logoPath);
            organizationRegister.setFile(filePath);
            organizationRegisterMapper.updateById(organizationRegister);

            return ResponseEntity.status(HttpStatus.OK).body(serialNumber);
        } else {
            System.out.println("fuck");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知错误");
        }
    }

    /**
     * 机构注册申请列表
     */
    @GetMapping("/organization/register/approval/list")
    public ResponseEntity<Object> organizationRegisterApplyList(HttpSession session) {
        User login = (User) session.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }

        LambdaQueryWrapper<OrganizationRegister> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrganizationRegister::getSuperior, login.getOrganization()).orderByDesc(OrganizationRegister::getApplyTime);

        List<OrganizationRegister> organizationRegisterList = organizationRegisterMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(organizationRegisterList);
    }

    /**
     * 查询注册申请进度
     */
    @PostMapping("/organization/register/apply/list")
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

        String newLogoPath = String.format("organization/%s/logo.jpg", orgID);
        String newFilePath = String.format("organization/%s/file.zip", orgID);
        // 云端复制文件
        minioService.copy(organizationRegister.getLogo(), newLogoPath);
        minioService.copy(organizationRegister.getFile(), newFilePath);
        // 存入数据库
        organization.setLogo(newLogoPath);
        organization.setFile(newFilePath);
        organizationMapper.updateById(organization);

        // 存储上链
        fabricService.saveOrganization(organization);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    /**
     * 判断机构是否存在
     */
    @PostMapping("/organization/exist")
    public ResponseEntity<Object> organizationExist(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getName, request.getString("name"));

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
        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Organization::getId, Organization::getName);

        List<Organization> organizationList = organizationMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(organizationList);
    }

    /**
     * 获取指定机构的信息
     */
    @PostMapping("/organization/information")
    public ResponseEntity<Object> organizationInformation(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        OrganizationInfo organizationInfo = organizationMapper.getOrganizationInformation(Long.parseLong(request.getString("id")));

        System.out.println(organizationInfo);

        return ResponseEntity.status(HttpStatus.OK).body(organizationInfo);
    }

    @GetMapping("/organization/subordinate/list")
    public ResponseEntity<Object> organizationSubordinateList(HttpSession session) {
        User login = (User) session.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }

        LambdaQueryWrapper<Organization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Organization::getSuperior, login.getOrganization());

        List<Organization> subordinateList = organizationMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(subordinateList);
    }
}
