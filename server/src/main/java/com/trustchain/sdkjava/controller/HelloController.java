package com.trustchain.sdkjava.controller;

import com.trustchain.sdkjava.fabric.FabricGateway;
import com.trustchain.sdkjava.fabric.FabricSDK;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/test")
    public String test() {
        FabricGateway fg = new FabricGateway();
        return fg.query("queryOrganizationList");
//        return fg.invoke("createAPI", "12345", "test", "", "4d7263c9-91e7-43c5-a06b-bad8290466be", "http://baidu.com", "get", "", "", "v1.0.0", "now");
    }
}
