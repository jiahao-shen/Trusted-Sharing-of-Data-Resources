package com.trustchain.sdkjava.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("test")
public class Fuck {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(value = "time")
    private Date time;

    @TableField(value = "type")
    private OrganizationType type;
}
