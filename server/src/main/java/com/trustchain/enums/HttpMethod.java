package com.trustchain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpMethod {
    GET(1, "GET"),
    POST(2, "POST"),
    PUT(3, "PUT"),
    DELETE(4, "DELETE"),
    TRACE(5, "TRACE"),
    CONNECT(6, "CONNECT"),
    HEAD(7, "HEAD"),
    OPTIONS(8, "OPTIONS");

    @EnumValue
    private final int code;
    private final String desp;
}
