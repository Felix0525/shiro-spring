package com.wuyue.shiro;

import lombok.Data;

import java.util.Optional;

@Data
public class DataResp<T> {

    private int code;

    private String msg;

    private T data;

    private DataResp() {
        this.code = StatusCode.SUCCESS.getCode();
        this.msg = StatusCode.SUCCESS.getMsg();
    }

    private DataResp(T data) {
        this();
        this.data = data;
    }

    private DataResp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> DataResp<T> build(int code, String msg) {
        return new DataResp<>(code, msg);
    }

    public static <T> DataResp<T> build(T data) {
        return new DataResp<>(data);
    }

    public static <T> DataResp<T> build(Optional<T> optData) {
        return new DataResp<>(optData.orElse(null));
    }

    public static <T> DataResp<T> build() {
        return new DataResp<>();
    }

}
