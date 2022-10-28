package com.trustchain.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum BodyType {
    FORM(1, "FORM"),
    JSON(2, "JSON");

    @EnumValue
    private final int code;
    private final String descp;
}
