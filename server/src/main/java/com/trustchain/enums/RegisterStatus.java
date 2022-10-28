package com.trustchain.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum RegisterStatus {
    PROCESSED(1, "待处理"),
    ALLOW(2, "通过"),
    REJECT(3, "驳回");

    @EnumValue
    private final int code;
    private final String descp;
}
