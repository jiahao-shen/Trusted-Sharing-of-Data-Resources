package com.trustchain.sdkjava.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trustchain.sdkjava.enums.APIInvokeMethod;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("api_invoke")
public class APIInvoke {
    @TableId(value = "serial_number", type = IdType.ASSIGN_ID)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long serialNumber;

    @TableField("id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @TableField("applicant")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long applicant;

    @TableField("author")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long author;

    @TableField("invoke_method")
    private APIInvokeMethod invokeMethod;

    @TableField("start_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @TableField("end_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @TableField("comment")
    private String comment;

    @TableField("created_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
}