package com.trustchain.sdkjava.controller;

import com.trustchain.sdkjava.fabric.FabricGateway;
import lombok.Data;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class APIController {
    private static final Logger logger = LogManager.getLogger(APIController.class);

    @PostMapping("/fabric/api/register")
    public ResponseEntity<Object> register(@RequestBody APIRegisterRequest request) {
        logger.info(request);

//        FabricGateway fg = new FabricGateway();
//        System.out.println(fg.query("queryOrganizationList"));
        UUID uuid = UUID.randomUUID();
        return ResponseEntity.status(HttpStatus.OK).body(uuid);
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


