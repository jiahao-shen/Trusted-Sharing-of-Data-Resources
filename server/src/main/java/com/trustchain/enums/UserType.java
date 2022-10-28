package com.trustchain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    ADMIN(1, "管理员"),
    NORMAL(2, "普通用户");

    @EnumValue
    private final int code;
    private final String descp;
}
