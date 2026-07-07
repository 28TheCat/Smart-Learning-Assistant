package com.wyt.controller;

import com.wyt.anno.Log;
import com.wyt.exception.BusinessException;
import com.wyt.exception.ErrorCode;
import com.wyt.pojo.LoginParam;
import com.wyt.pojo.LoginInfo;
import com.wyt.pojo.Result;
import com.wyt.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private EmpService empService;

    @Log
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginParam loginParam) {
        log.info("员工登录 username={}", loginParam.getUsername());
        LoginInfo loginInfo = empService.login(loginParam);
        if (loginInfo == null) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        return Result.success(loginInfo);
    }
}
