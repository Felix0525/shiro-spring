package com.wuyue.shiro;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;

public enum StatusCode {

    SUCCESS(0, "Success"),
    AUTHENTICATION_ERROR(1001, "认证失败"),
    AUTHORIZATION_ERROR(1002, "权限不足"),
    SYSTEM_ERROR(9999, "System error");

    @Getter
    private int code;

    @Getter
    private String msg;

    StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private static Map<Integer, StatusCode> ENUM_MAP = new HashMap<>();
    static {
        Stream.of(StatusCode.values())
            .collect(Collectors.toMap(StatusCode::getCode, e -> e, (e1, e2) -> e1));
    }

    public StatusCode asEnum(Integer code) {
        return ENUM_MAP.get(code);
    }

}
