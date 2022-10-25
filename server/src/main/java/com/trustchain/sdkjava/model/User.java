package com.trustchain.sdkjava.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.alibaba.fastjson.serializer.ToStringSerializer;


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
    @JSONField(serialize = false)
    private String password;

    @TableField("organization")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long organization;

    @TableField("created_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
}

