package com.trustchain.controller;

import com.alibaba.fastjson.JSONObject;
import com.trustchain.fabric.FabricGateway;
import com.trustchain.mapper.OrganizationRegisterMapper;
import com.trustchain.service.MinioService;
import com.trustchain.model.OrganizationRegister;
import com.trustchain.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Future;

@RestController
public class HelloController {
    @Autowired
    private OrganizationRegisterMapper organizationRegisterMapper;

    @Autowired
    private MinioService minioService;

    @Autowired
    private HelloService service;

    @GetMapping("/hello")
    public String hello() throws Exception{
//        service.testAsync();
        Future<Boolean> result = service.testAsyncResult();

        return "Hello World";
    }

    @PostMapping("/test")
    public String test(@RequestBody JSONObject request) {
//        System.out.println(request.getString("name"));
//        JSONArray users = request.getJSONArray("users");
//        System.out.println(users);
//        Integer age = request.get("age");

        return "Test";
    }

    @GetMapping("/fuck")
    public ResponseEntity<Object> fuck() {
        OrganizationRegister ro = organizationRegisterMapper.selectById("1583321439534010370");

        return ResponseEntity.status(HttpStatus.OK).body(ro);
    }

    @GetMapping("/test/fabric")
    public ResponseEntity<Object> testFabric() {
        FabricGateway fg = new FabricGateway();
        return ResponseEntity.status(HttpStatus.OK).body(fg.query("queryAPIList"));
    }

    @PostMapping("/test/minio")
    public ResponseEntity<Object> testMinio(@RequestPart("file") MultipartFile file) {
        try {
            minioService.upload(file, "test.jpg");
            return ResponseEntity.status(HttpStatus.OK).body("good");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body("no");
        }
    }
}
