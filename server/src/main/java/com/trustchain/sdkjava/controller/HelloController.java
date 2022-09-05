package com.trustchain.sdkjava.controller;

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
        FabricSDK fs = new FabricSDK();
        return fs.query("queryOrganizationList");
    }
}
