package com.trustchain.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trustchain.fabric.FabricGateway;
import com.trustchain.mapper.APIMapper;
import com.trustchain.mapper.APIRegisterMapper;
import com.trustchain.model.*;
import com.trustchain.enums.APIInvokeMethod;
import com.trustchain.enums.BodyType;
import com.trustchain.enums.HttpMethod;
import com.trustchain.enums.RegisterStatus;
import com.trustchain.mapper.APIInvokeMapper;
import com.trustchain.service.FabricService;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class APIController {
    private static final Logger logger = LogManager.getLogger(APIController.class);

    @Autowired
    private APIRegisterMapper apiRegisterMapper;

    @Autowired
    private APIMapper apiMapper;


    @Autowired
    private APIInvokeMapper apiInvokeMapper;

    @Autowired
    private FabricService fabricService;

    /**
     * 发起API注册申请
     */
    @PostMapping("/api/register/apply")
    public ResponseEntity<Object> apiRegisterApply(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);
        User login = (User) session.getAttribute("login");

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

    }

    /**
     * 获取别人发起的注册申请
     */
    @GetMapping("/api/register/apply/list")
    public ResponseEntity<Object> apiRegisterApplyList(HttpSession sesssion) {
        User login = (User) sesssion.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
        }

        LambdaQueryWrapper<APIRegister> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(APIRegister::getOrganization, login.getOrganization()).orderByDesc(APIRegister::getApplyTime);

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

        // 存储上链
        fabricService.saveAPI(api);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }


    @GetMapping("/api/list/my")
    public ResponseEntity<Object> myAPIList(HttpSession session) {
        User login = (User) session.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("机构API失败");
        }

        LambdaQueryWrapper<API> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(API::getAuthor, login.getId());

        List<API> myAPIList = apiMapper.selectList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(myAPIList);
    }

    @GetMapping("/api/list/all")
    public ResponseEntity<Object> allAPIList(HttpSession session) {

        LambdaQueryWrapper<API> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(API::getId).orderByAsc(API::getCreatedTime);

        List<APIInfo> allAPIList = apiMapper.getAllAPIList(queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(allAPIList);
    }


    @PostMapping("/api/invoke/apply")
    public ResponseEntity<Object> apiInvokeApply(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        User login = (User) session.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
        }

        API api = apiMapper.selectById(request.getString("id"));

        APIInvoke apiInvoke = new APIInvoke();
        apiInvoke.setId(Long.parseLong(request.getString("id")));
        apiInvoke.setApplicant(Long.parseLong(request.getString("applicant")));
        apiInvoke.setAuthor(api.getAuthor());
        apiInvoke.setInvokeMethod(APIInvokeMethod.valueOf(request.getString("invokeMethod")));

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            apiInvoke.setStartTime(format.parse(request.getString("startTime")));
            apiInvoke.setEndTime(format.parse(request.getString("endTime")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("日期转换错误");
        }
        apiInvoke.setStatus(RegisterStatus.PROCESSED);
        apiInvoke.setComment(request.getString("comment"));
        apiInvoke.setApplyTime(new Date());

        logger.info(apiInvoke);

        int count = apiInvokeMapper.insert(apiInvoke);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("调用申请失败");
        }

        return ResponseEntity.status(HttpStatus.OK).body("调用申请成功");
    }

    @GetMapping("/api/invoke/apply/list")
    public ResponseEntity<Object> apiInvokeApplyList(HttpSession session) {
        User login = (User) session.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
        }

        LambdaQueryWrapper<APIInvoke> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(APIInvoke::getApplyTime);

        List<APIInvokeApplyInfo> apiInvokeApplyList = apiInvokeMapper.getAPIInvokeApplyList(login.getId(), queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(apiInvokeApplyList);
    }

    @GetMapping("/api/invoke/approval/list")
    public ResponseEntity<Object> apiInvokeApprovalList(HttpSession session) {
        User login = (User) session.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
        }

        LambdaQueryWrapper<APIInvoke> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(APIInvoke::getApplyTime);

        List<APIInvokeApprovalInfo> apiInvokeApprovalList = apiInvokeMapper.getAPIInvokeApprovalList(login.getId(), queryWrapper);

        return ResponseEntity.status(HttpStatus.OK).body(apiInvokeApprovalList);
    }

    @PostMapping("/api/invoke/reply")
    public ResponseEntity<Object> apiInvokeReply(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);

        RegisterStatus reply = RegisterStatus.valueOf(request.getString("reply"));

        APIInvoke apiInvoke = apiInvokeMapper.selectById(Long.parseLong(request.getString("serialNumber")));

        apiInvoke.setStatus(reply);
        if (reply == RegisterStatus.REJECT) {
            String reason = request.getString("reason");
            apiInvoke.setReplyMessage(reason);
        }
        apiInvoke.setReplyTime(new Date());

        apiInvokeMapper.updateById(apiInvoke);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PostMapping("/api/invoke/web")
    public ResponseEntity<Object> apiInvokeByWeb(@RequestBody JSONObject request, HttpSession session) {
        logger.info(request);
        User login = (User) session.getAttribute("login");

        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("请重新登录");
        }

        // TODO: 查验有效期
        // TODO: 查验审批状态
        // TODO: 查验调用方式

        Object response = fabricService.invokeAPI(request.getString("applicant"), request.getString("id"), "", "");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}



