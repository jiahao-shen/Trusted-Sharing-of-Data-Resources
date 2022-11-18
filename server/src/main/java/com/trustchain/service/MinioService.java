package com.trustchain.service;

import com.trustchain.minio.MinioConfig;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioService {
    private MinioConfig config;

    private MinioClient client;

    @Autowired
    MinioService(MinioConfig config) {
        this.config = config;

        client = MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();

    }

    // 上传文件
    @Async
    public void upload(MultipartFile file, String path) {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(config.getBucket())
                    .object(path)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: 下载文件
    public void download() throws Exception {
    }

    // 复制文件
    @Async
    public void copy(String oldPath, String newPath) {
        try {
            client.copyObject(CopyObjectArgs.builder()
                    .bucket(config.getBucket())
                    .object(newPath)
                    .source(CopySource.builder()
                            .bucket(config.getBucket())
                            .object(oldPath).build())
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createBucket(String name) throws Exception {
        client.makeBucket(MakeBucketArgs.builder().bucket(name).build());
    }

    public void listBuckets() throws Exception {
        client.listBuckets().forEach(b -> {
            System.out.println(b.name());
        });
    }

    public void listFiles() throws Exception {
        client.listObjects(ListObjectsArgs.builder().bucket(config.getBucket()).recursive(false).build()).forEach(r -> {
            try {
                System.out.println(r.get().objectName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
