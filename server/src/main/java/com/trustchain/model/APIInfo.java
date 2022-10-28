package com.trustchain.model;

import com.trustchain.enums.OrganizationType;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
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
