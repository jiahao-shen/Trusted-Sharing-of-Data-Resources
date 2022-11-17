package com.trustchain.minio;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.ErrorResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
public class MinioUtil {
    @Autowired
    private MinioClient client;
    private final String bucket = "trustchain";

    public void upload(MultipartFile file, String path) throws Exception {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(path)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        }
    }

    public void download() throws Exception {

    }

    public void listBuckets() throws Exception {
        client.listBuckets().forEach(b -> {
            System.out.println(b.name());
        });
    }

    public void listFiles() throws Exception {
        client.listObjects(ListObjectsArgs.builder().bucket(bucket).recursive(false).build()).forEach(r -> {
            try {
                System.out.println(r.get().objectName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
