package com.trustchain.minio;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.lang.reflect.Type;

@Component
@NoArgsConstructor
public class MinioURLSerializer implements ObjectSerializer {

    private static MinioConfig config;

    @Autowired
    public MinioURLSerializer(MinioConfig config) {
        MinioURLSerializer.config = config;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fileName, Type type, int features) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }

        String path = config.getEndpoint() + "/" + config.getBucket() + "/" + object;

        serializer.write(path);
    }
}
