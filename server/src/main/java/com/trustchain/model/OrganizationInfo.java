package com.trustchain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrganizationInfo extends Organization {
    @TableField("superior_name")
    private String superiorName;
}