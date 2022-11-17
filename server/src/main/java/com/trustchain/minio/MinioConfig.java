package com.trustchain.minio;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    private final String endpoint = "http://192.168.100.78:9000";
//    private final String endpoint = "http://127.0.0.1:9000";
    private final String accessKey = "minioadmin";
    private final String secretKey = "minioadmin";

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
