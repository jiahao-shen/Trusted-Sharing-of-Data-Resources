package com.trustchain.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.trustchain.enums.BodyType;
import com.trustchain.enums.HttpMethod;
import com.trustchain.enums.RegisterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_register")
public class APIRegister {
    @TableId(value = "serial_number", type = IdType.ASSIGN_ID)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long serialNumber;

    @TableField("id")
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

    @TableField("status")
    private RegisterStatus status;

    @TableField("apply_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;    // 申请时间

    @TableField("reply_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;   // 批复时间

    @TableField("reply_message")
    private String replyMessage;    // 批复内容
}
