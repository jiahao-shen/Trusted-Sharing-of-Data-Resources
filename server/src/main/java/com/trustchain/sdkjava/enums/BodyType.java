package com.trustchain.sdkjava.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BodyType {
    FORM(1, "FORM"),
    JSON(2, "JSON");

    @EnumValue
    private final int code;
    private final String descp;
}
