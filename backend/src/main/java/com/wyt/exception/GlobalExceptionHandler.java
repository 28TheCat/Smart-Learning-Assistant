package com.wyt.exception;

import com.wyt.pojo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthException.class)
    public Result handleAuthException(AuthException e, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        log.warn("未登录或令牌失效 method={} uri={} errorCode={} message={}",
                request.getMethod(), request.getRequestURI(), e.getErrorCode().getCode(), e.getMessage());
        return Result.error(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常 method={} uri={} errorCode={} message={}",
                request.getMethod(), request.getRequestURI(), e.getErrorCode().getCode(), e.getMessage());
        return Result.error(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }
        String message = firstMessage(errors, ErrorCode.PARAM_ERROR.getMessage());
        log.warn("请求体校验失败 method={} uri={} errorCode={} errors={}",
                request.getMethod(), request.getRequestURI(), ErrorCode.PARAM_ERROR.getCode(), errors);
        return Result.error(ErrorCode.PARAM_ERROR, message, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.putIfAbsent(simplePropertyName(violation.getPropertyPath().toString()), violation.getMessage());
        }
        String message = firstMessage(errors, ErrorCode.PARAM_ERROR.getMessage());
        log.warn("请求参数校验失败 method={} uri={} errorCode={} errors={}",
                request.getMethod(), request.getRequestURI(), ErrorCode.PARAM_ERROR.getCode(), errors);
        return Result.error(ErrorCode.PARAM_ERROR, message, errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public Result handleHandlerMethodValidationException(HandlerMethodValidationException e, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getParameterValidationResults().forEach(result -> {
            String parameterName = result.getMethodParameter().getParameterName();
            String key = parameterName == null ? "param" : parameterName;
            result.getResolvableErrors().forEach(error ->
                    errors.putIfAbsent(key, error.getDefaultMessage()));
        });
        String message = firstMessage(errors, ErrorCode.PARAM_ERROR.getMessage());
        log.warn("方法参数校验失败 method={} uri={} errorCode={} errors={}",
                request.getMethod(), request.getRequestURI(), ErrorCode.PARAM_ERROR.getCode(), errors);
        return Result.error(ErrorCode.PARAM_ERROR, message, errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException e,
                                                                HttpServletRequest request) {
        String message = e.getParameterName() + "不能为空";
        log.warn("缺少请求参数 method={} uri={} errorCode={} parameter={}",
                request.getMethod(), request.getRequestURI(), ErrorCode.PARAM_ERROR.getCode(), e.getParameterName());
        return Result.error(ErrorCode.PARAM_ERROR, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                            HttpServletRequest request) {
        String message = e.getName() + "格式不正确";
        log.warn("请求参数类型错误 method={} uri={} errorCode={} parameter={} value={}",
                request.getMethod(), request.getRequestURI(), ErrorCode.PARAM_ERROR.getCode(), e.getName(), e.getValue());
        return Result.error(ErrorCode.PARAM_ERROR, message);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        log.warn("上传文件过大 method={} uri={} errorCode={} message={}",
                request.getMethod(), request.getRequestURI(), ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        return Result.error(ErrorCode.PARAM_ERROR, "上传文件大小不能超过10MB");
    }

    @ExceptionHandler(MultipartException.class)
    public Result handleMultipartException(MultipartException e, HttpServletRequest request) {
        log.warn("上传请求解析失败 method={} uri={} errorCode={} message={}",
                request.getMethod(), request.getRequestURI(), ErrorCode.PARAM_ERROR.getCode(), e.getMessage());
        return Result.error(ErrorCode.PARAM_ERROR, "上传文件不能为空或请求格式不正确");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("请求体解析失败 method={} uri={} errorCode={} message={}",
                request.getMethod(), request.getRequestURI(), ErrorCode.REQUEST_BODY_ERROR.getCode(), e.getMessage());
        return Result.error(ErrorCode.REQUEST_BODY_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        log.warn("数据约束异常 method={} uri={} errorCode={} message={}",
                request.getMethod(), request.getRequestURI(), ErrorCode.DATA_CONFLICT.getCode(), e.getMostSpecificCause().getMessage());
        return Result.error(ErrorCode.DATA_CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 method={} uri={} errorCode={}",
                request.getMethod(), request.getRequestURI(), ErrorCode.SYSTEM_ERROR.getCode(), e);
        return Result.error(ErrorCode.SYSTEM_ERROR);
    }

    private String firstMessage(Map<String, String> errors, String defaultMessage) {
        return errors.values().stream().findFirst().orElse(defaultMessage);
    }

    private String simplePropertyName(String propertyPath) {
        int dotIndex = propertyPath.lastIndexOf('.');
        return dotIndex >= 0 ? propertyPath.substring(dotIndex + 1) : propertyPath;
    }
}

