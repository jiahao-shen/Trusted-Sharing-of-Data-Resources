package com.trustchain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class APIInvokeApplyInfo extends APIInvoke {
    @TableField("api_name")
    private String apiName;

    @TableField("organiation_name")
    private String organizationName;
}
