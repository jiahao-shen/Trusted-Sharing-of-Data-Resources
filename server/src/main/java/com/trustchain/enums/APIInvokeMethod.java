package com.trustchain.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.EnumValue;

@Getter
@AllArgsConstructor
public enum APIInvokeMethod {
    WEB(1, "网页"),
    SDK(2, "SDK");

    @EnumValue
    private final int code;
    private final String descp;
}
