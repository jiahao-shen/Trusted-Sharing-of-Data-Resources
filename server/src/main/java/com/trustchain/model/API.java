package com.trustchain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trustchain.enums.BodyType;
import com.trustchain.enums.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("api")
public class API {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("author")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long author;

    @TableField("organization")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long organization;

    @TableField("url")
    private String url;

    @TableField("method")
    private HttpMethod method;

    @TableField("introduction")
    private String introducation;

    @TableField("category")
    private String category;

    @TableField("authorize")
    private String authorize;

    @TableField("version")
    private String version;

    @TableField("header_type")
    private BodyType headerType;

    @TableField("header")
    private String header;

    @TableField("request_type")
    private BodyType requestType;

    @TableField("request")
    private String request;

    @TableField("response_type")
    private BodyType responseType;

    @TableField("response")
    private String response;

    @TableField("created_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;   // 创建时间
}
