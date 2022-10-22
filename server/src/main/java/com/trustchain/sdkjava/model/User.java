package com.trustchain.sdkjava.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("organization")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long organization;

    @TableField("created_time")
    private Date createdTime;
}

