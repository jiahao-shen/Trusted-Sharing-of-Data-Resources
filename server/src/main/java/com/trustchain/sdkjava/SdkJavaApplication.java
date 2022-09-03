package com.trustchain.sdkjava;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.trustchain.sdkjava.mapper")
public class SdkJavaApplication {
    private static final Logger logger = LogManager.getLogger(SdkJavaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SdkJavaApplication.class, args);
    }
}
