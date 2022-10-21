package com.trustchain.sdkjava.model;

import com.baomidou.mybatisplus.annotation.*;
import com.trustchain.sdkjava.enums.OrganizationType;
import com.trustchain.sdkjava.enums.RegisterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("register_organization")
public class RegisterOrganization {
    @TableId(value = "serial_number", type = IdType.ASSIGN_ID)
    private Long serialNumber;  // 注册流水号

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
    private Long superior; // 上级机构

    @TableField("provide_node")
    private boolean provideNode;    // 是否提供节点

    @TableField("num_nodes")
    private int numNodes;   // 节点数量

    @TableField("file")
    private String file;    // 机构文件

    @TableField("status")
    private RegisterStatus status;  // 申请状态

    @TableField(value = "apply_time")
    private Date applyTime;    // 申请时间

    @TableField(value = "reply_time")
    private Date replyTime;   // 批复时间

    @TableField(value = "reply_message")
    private String replyMessage;    // 批复内容
}
