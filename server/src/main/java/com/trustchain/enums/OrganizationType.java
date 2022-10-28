package com.trustchain.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.EnumValue;


@Getter
@AllArgsConstructor
public enum OrganizationType {
    MEDICAL(1, "医疗"),
    EDUCATION(2, "教育"),
    FINANCIAL(3, "金融"),
    GOVERNMENT(4, "政府");

    @EnumValue
    private final int code;
    private final String desp;
}
