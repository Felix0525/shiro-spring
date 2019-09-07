package com.wuyue.shiro.exception;

import lombok.Getter;

import java.util.Optional;

public class BizException extends RuntimeException {

    @Getter
    private Optional<Integer> code;

    public BizException(String msg) {
        super(msg);
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = Optional.of(code);
    }

}
