package com.trustchain.sdkjava.model;

import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import com.trustchain.sdkjava.enums.OrganizationType;
import com.baomidou.mybatisplus.annotation.TableField;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class APIInfo extends API {
    @TableField("organization_name")
    private String organizationName;

    @TableField("organization_type")
    private OrganizationType organizationType;
}
