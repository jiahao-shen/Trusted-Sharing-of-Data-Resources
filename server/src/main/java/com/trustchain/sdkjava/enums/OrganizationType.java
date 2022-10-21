package com.trustchain.sdkjava.model;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonValue;
import com.baomidou.mybatisplus.annotation.EnumValue;


@Getter
@AllArgsConstructor
public enum OrganizationType {
    MEDICAL(1, "医疗"),
    EDUCATION(2, "教育"),
    FINANCIAL(3, "金融"),
    GOVERNMENT(4, "政府");

    @EnumValue
//    @JsonValue
    private final int code;
    private final String desp;
}
