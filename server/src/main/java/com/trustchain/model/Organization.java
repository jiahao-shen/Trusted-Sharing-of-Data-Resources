package com.trustchain.model;

import com.trustchain.enums.OrganizationType;
import com.trustchain.minio.MinioURLSerializer;
import lombok.Data;

import java.util.Date;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.alibaba.fastjson.serializer.ToStringSerializer;


@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("organization")
public class Organization {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;  // 机构ID

    @TableField("name")
    private String name;    // 机构名称

    @TableField("logo")
    @JSONField(serializeUsing = MinioURLSerializer.class)
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
    @JSONField(serializeUsing = MinioURLSerializer.class)
    private String file;    // 机构文件

    @TableField("created_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;   // 创建时间
}
