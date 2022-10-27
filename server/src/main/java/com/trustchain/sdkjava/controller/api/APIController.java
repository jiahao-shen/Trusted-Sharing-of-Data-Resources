package com.trustchain.sdkjava.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.protobuf.Api;
import com.trustchain.sdkjava.enums.BodyType;
import com.trustchain.sdkjava.enums.HttpMethod;
import com.trustchain.sdkjava.enums.RegisterStatus;
import com.trustchain.sdkjava.fabric.FabricGateway;
import com.trustchain.sdkjava.mapper.APIMapper;
import com.trustchain.sdkjava.mapper.APIRegisterMapper;
import com.trustchain.sdkjava.model.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
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
public class APIController {
    private static final Logger logger = LogManager.getLogger(APIController.class);

    @Autowired
    private APIRegisterMapper apiRegisterMapper;

    @Autowired
    private APIMapper apiMapper;

    /**
     * 发起API注册申请
     */
    @PostMapping("/api/register/apply")
    public ResponseEntity<Object> apiRegisterApply(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);
        User login = (User) session.getAttribute("user");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登陆");
        }

        APIRegister apiRegister = new APIRegister();
        apiRegister.setName(request.getString("name"));
        apiRegister.setAuthor(login.getId());
        apiRegister.setOrganization(login.getOrganization());
        apiRegister.setUrl(request.getString("url"));
        apiRegister.setMethod(HttpMethod.valueOf(request.getString("method")));
        apiRegister.setIntroducation(request.getString("introduction"));
        apiRegister.setCategory(request.getString("category"));
        apiRegister.setAuthorize(request.getString("authorize"));
        apiRegister.setVersion(request.getString("version"));
        apiRegister.setHeaderType(BodyType.valueOf(request.getString("headerType")));
        apiRegister.setHeader(request.getString("header"));
        apiRegister.setRequestType(BodyType.valueOf(request.getString("requestType")));
        apiRegister.setRequest(request.getString("request"));
        apiRegister.setResponseType(BodyType.valueOf(request.getString("responseType")));
        apiRegister.setResponse(request.getString("response"));
        apiRegister.setStatus(RegisterStatus.PROCESSED);
        apiRegister.setApplyTime(new Date());

        int count = apiRegisterMapper.insert(apiRegister);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知错误");
        }

        return ResponseEntity.status(HttpStatus.OK).body(true);
//        User user = (User) session.getAttribute("user");
//
//        try {
//            FabricGateway fg = new FabricGateway();
//            String response = fg.invoke("createAPI", UUID.randomUUID().toString(), request.getName(),
//                    request.getIntroduction(), user.getFabricID(), request.getUrl(), request.getMethod(),
//                    request.getHeader(), request.getHeaderType(),
//                    request.getRequest(), request.getRequestType(),
//                    request.getResponse(), request.getResponseType(),
//                    request.getVersion(), LocalDateTime.now().toString());
//
//            return ResponseEntity.status(HttpStatus.OK).body(response);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
//        }
    }

    /**
     * 获取别人发起的注册申请
     */
    @GetMapping("/api/register/apply/list")
    public ResponseEntity<Object> apiRegisterApplyList(HttpSession sesssion) {
        User login = (User) sesssion.getAttribute("user");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
        }

        QueryWrapper<APIRegister> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("organization", login.getOrganization()).orderByDesc("apply_time");
        List<APIRegister> apiRegisterList = apiRegisterMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(apiRegisterList);
    }

    @PostMapping("/api/register/reply")
    public ResponseEntity<Object> apiRegisterReply(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));

        APIRegister apiRegister = apiRegisterMapper.selectById(Long.parseLong(request.getString("serialNumber")));


        API api = new API();
        api.setAuthor(apiRegister.getAuthor());
        api.setOrganization(apiRegister.getOrganization());
        api.setName(apiRegister.getName());
        api.setUrl(apiRegister.getUrl());
        api.setMethod(apiRegister.getMethod());
        api.setIntroducation(apiRegister.getIntroducation());
        api.setCategory(apiRegister.getCategory());
        api.setAuthorize(apiRegister.getAuthorize());
        api.setVersion(apiRegister.getVersion());
        api.setHeaderType(apiRegister.getHeaderType());
        api.setHeader(apiRegister.getHeader());
        api.setRequestType(apiRegister.getRequestType());
        api.setRequest(apiRegister.getRequest());
        api.setResponseType(apiRegister.getResponseType());
        api.setResponse(apiRegister.getResponse());
        api.setCreatedTime(new Date());

        int count = apiMapper.insert(api);

        if (count == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("机构API失败");
        }

        Long apiID = api.getId();
        apiRegister.setId(apiID);
        apiRegister.setStatus(reply);
        if (reply == RegisterStatus.REJECT) {
            String reason = request.getString("reason");
            apiRegister.setReplyMessage(reason);
        }
        apiRegister.setReplyTime(new Date());
        apiRegisterMapper.updateById(apiRegister);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }


    @GetMapping("/api/list/my")
    public ResponseEntity<Object> myAPIList(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("机构API失败");
        }

        QueryWrapper<API> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("author", user.getId());
        List<API> myAPIList = apiMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(myAPIList);
    }

    @GetMapping("/api/list/all")
    public ResponseEntity<Object> allAPIList(HttpSession session) {
        List<APIInfo> allAPIList = apiMapper.selectJoinList(APIInfo.class,
                new MPJLambdaWrapper<API>()
                        .selectAll(API.class)
                        .selectAs(Organization::getName, APIInfo::getOrganizationName)
                        .selectAs(Organization::getType, APIInfo::getOrganizationType)
                        .leftJoin(Organization.class, Organization::getId, API::getOrganization)
                        .orderByAsc(API::getId)
                        .orderByAsc(API::getCreatedTime));

        return ResponseEntity.status(HttpStatus.OK).body(allAPIList);
    }


    @PostMapping("/api/invoke/apply")
    public ResponseEntity<Object> call(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Invoke Apply");
//        try {
//            FabricGateway fg = new FabricGateway();
//            String response = fg.invoke("requestAPI", UUID.randomUUID().toString(), user.getFabricID(),
//                    request.getApiID(), request.getApiRequest(), LocalDateTime.now().toString());
//            return ResponseEntity.status(HttpStatus.OK).body(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
//        }
    }
}



