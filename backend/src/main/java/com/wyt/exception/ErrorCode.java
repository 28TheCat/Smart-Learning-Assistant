package com.wyt.exception;

public enum ErrorCode {
    PARAM_ERROR("PARAM_ERROR", "请求参数不正确"),
    LOGIN_FAILED("LOGIN_FAILED", "用户名或密码错误"),
    BUSINESS_ERROR("BUSINESS_ERROR", "业务处理失败"),
    DATA_CONFLICT("DATA_CONFLICT", "数据已存在或违反约束"),
    REQUEST_BODY_ERROR("REQUEST_BODY_ERROR", "请求体格式不正确"),
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常，请联系管理员");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
