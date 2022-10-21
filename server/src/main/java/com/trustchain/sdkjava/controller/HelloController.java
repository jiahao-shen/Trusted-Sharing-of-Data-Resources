package com.trustchain.sdkjava.controller;

import java.util.ArrayList;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trustchain.sdkjava.mapper.RegisterOrganizationMapper;
import com.trustchain.sdkjava.model.RegisterOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private RegisterOrganizationMapper registerOrganizationMapper;

    @GetMapping("/hello")
    public String hello() {
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
        RegisterOrganization ro = registerOrganizationMapper.selectById("1583321439534010370");

        return ResponseEntity.status(HttpStatus.OK).body(ro);
    }
}
