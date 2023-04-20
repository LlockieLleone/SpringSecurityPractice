package com.lingjun.springsecuritypractice.model.common;

import lombok.Data;

@Data
public class Response<T> {

    public static final int SUCCESS = 200;

    public static final int FAIL = 500;

    private int code;

    private String msg;

    private T data;

    private static <T> Response<T> restResult(T data, int code, String msg) {
        Response<T> apiResult = new Response<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public static <T> Response<T> success() {
        return restResult(null, SUCCESS, null);
    }

    public static <T> Response<T> success(String msg) {
        return restResult(null, SUCCESS, msg);
    }

    public static <T> Response<T> success(T data) {
        return restResult(data, SUCCESS, null);
    }

    public static <T> Response<T> success(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> Response<T> fail() {
        return restResult(null, FAIL, null);
    }

    public static <T> Response<T> fail(String msg) {
        return restResult(null, FAIL, msg);
    }

    public static <T> Response<T> fail(T data) {
        return restResult(data, FAIL, null);
    }

    public static <T> Response<T> fail(T data, String msg) {
        return restResult(data, FAIL, msg);
    }
}
