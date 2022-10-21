package com.trustchain.sdkjava.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("organization")
public class Organization {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;  // 机构ID

    @TableField("name")
    private String name;    // 机构名称

    @TableField("logo")
    private String logo; // 机构Logo

    @TableField("type")
    private OrganizationType type;    // 机构类型

    @TableField("telephone")
    private String telephone;   // 机构电话

    @TableField("email")
    private String email;   // 机构邮箱

    @TableField("city")
    private String city;    // 机构城市

    @TableField("address")
    private String address; // 机构地址

    @TableField("introduction")
    private String introduction;    // 机构介绍

    @TableField("superior")
    private String superior; // 上级机构

    @TableField("provide_node")
    private boolean provideNode;    // 是否提供节点

    @TableField("num_nodes")
    private int numNodes;   // 节点数量

    @TableField("file")
    private String file;    // 机构文件

    @TableField("created_time")
    private Date CreatedTime;   // 创建时间

//    @TableField("api_list")
//    private ArrayList<String> apiList; // API列表
//
//    @TableField("user_list")
//    private ArrayList<String> userList;   // 用户列表
}
