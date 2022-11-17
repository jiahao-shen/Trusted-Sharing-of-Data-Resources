package com.trustchain.minio;

import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
public class MinioUtil {
    private MinioConfig config;

    private MinioClient client;

    @Autowired
    MinioUtil(MinioConfig config) {
        this.config = config;

        client = MinioClient.builder()
                .endpoint(config.getEndpoint())
                .credentials(config.getAccessKey(), config.getSecretKey())
                .build();

    }

    // 上传文件
    public void upload(MultipartFile file, String path) throws Exception {
        client.putObject(PutObjectArgs.builder()
                .bucket(config.getBucket())
                .object(path)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
    }

    // TODO: 下载文件
    public void download() throws Exception {
    }

    // 复制文件
    public void copy(String oldPath, String newPath) throws Exception {
        client.copyObject(CopyObjectArgs.builder()
                .bucket(config.getBucket())
                .object(newPath)
                .source(CopySource.builder()
                        .bucket(config.getBucket())
                        .object(oldPath).build())
                .build());
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
