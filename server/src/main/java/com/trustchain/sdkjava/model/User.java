package com.trustchain.sdkjava.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {
    @TableId(value="username", type= IdType.INPUT)
    private String username;

    @TableField("password")
    private String password;

    @TableField("image_url")
    private String imageURL;

    @TableField("fabric_id")
    private String fabricID;
}

