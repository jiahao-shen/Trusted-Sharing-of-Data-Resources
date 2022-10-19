package com.trustchain.sdkjava.controller;

import com.trustchain.sdkjava.mapper.RegisterOrganizationMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class RegisterController {
    @Autowired
    private RegisterOrganizationMapper organizationRegisterMapper;

    private static final Logger logger = LogManager.getLogger(RegisterController.class);

    @PostMapping("/register/organization")
    public ResponseEntity<Object> registerOrganization(@RequestBody String request, HttpSession session) {
        logger.info("OK");

        return ResponseEntity.status(HttpStatus.OK).body("Good");
    }

}
