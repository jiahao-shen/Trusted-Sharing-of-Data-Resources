package com.trustchain.sdkjava.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("User")
public class User {
    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("image_url")
    private String imageURL;
}

