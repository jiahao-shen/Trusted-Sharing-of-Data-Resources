package com.trustchain.sdkjava.controller;

import com.trustchain.sdkjava.fabric.FabricGateway;
import lombok.Data;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
public class APIController {
    private static final Logger logger = LogManager.getLogger(APIController.class);

    @PostMapping("/fabric/api/register")
    public ResponseEntity<Object> register(@RequestBody APIRegisterRequest request) {
        logger.info(request);

        FabricGateway fg = new FabricGateway();

        String response = fg.invoke("createAPI", UUID.randomUUID().toString(), request.getName(),
                request.getIntroduction(), "test", request.getUrl(), request.getMethod(),
                request.getHeader(), request.getHeaderType(),
                request.getRequest(), request.getRequestType(),
                request.getResponse(), request.getResponseType(),
                request.getVersion(), LocalDateTime.now().toString());

        logger.info(response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/fabric/api/myList")
    public ResponseEntity<Object> myList() {
        // TODO:
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/fabric/api/allList")
    public ResponseEntity<Object> allList() {
        // TODO: 筛选处理
        // TODO: 页面处理
        // TODO: 加密处理
        FabricGateway fg = new FabricGateway();

        String response = fg.query("queryAPIList");

        logger.info(response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

@Data
class APIRegisterRequest {
    private String name;
    private String url;
    private String method;
    private String introduction;
    private String category;
    private String version;
    private String permission;
    private String header;
    private String headerType;
    private String request;
    private String requestType;
    private String response;
    private String responseType;
}


