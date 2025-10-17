package com.wyt.controller;

import com.wyt.anno.Log;
import com.wyt.pojo.Emp;
import com.wyt.pojo.LoginInfo;
import com.wyt.pojo.Result;
import com.wyt.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private EmpController empController;
    @Autowired
    private EmpService empService;
    @Log
    @PostMapping("/login")
    public Result login(@RequestBody Emp  emp) {
        log.info("登录：{}", emp);
        LoginInfo loginInfo = empService.login(emp);
        if (loginInfo == null) {
            return Result.error("用户名或密码错误");
        }
        return Result.success(loginInfo);
    }
}
