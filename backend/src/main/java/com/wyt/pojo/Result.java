package com.wyt.pojo;

import com.wyt.exception.ErrorCode;
import lombok.Data;

/**
 * 后端统一返回结果
 */
@Data
public class Result {

    private static final int SUCCESS_CODE = 1;
    private static final int FAILURE_CODE = 0;

    private Integer code; //编码：1成功，0为失败
    private String errorCode; //失败时的细分错误码
    private String msg; //错误信息
    private Object data; //数据

    public static Result success() {
        Result result = new Result();
        result.code = SUCCESS_CODE;
        result.msg = "success";
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.data = object;
        result.code = SUCCESS_CODE;
        result.msg = "success";
        return result;
    }

    public static Result error(String msg) {
        return error(ErrorCode.BUSINESS_ERROR, msg);
    }

    public static Result error(ErrorCode errorCode) {
        return error(errorCode, errorCode.getMessage());
    }

    public static Result error(ErrorCode errorCode, String msg) {
        return error(errorCode, msg, null);
    }

    public static Result error(ErrorCode errorCode, String msg, Object data) {
        ErrorCode resolvedErrorCode = errorCode == null ? ErrorCode.SYSTEM_ERROR : errorCode;
        Result result = new Result();
        result.msg = (msg == null || msg.isBlank()) ? resolvedErrorCode.getMessage() : msg;
        result.code = FAILURE_CODE;
        result.errorCode = resolvedErrorCode.getCode();
        result.data = data;
        return result;
    }

}
