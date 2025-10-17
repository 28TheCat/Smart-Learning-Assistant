package com.wyt.controller;

import com.wyt.pojo.*;
import com.wyt.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/log")
@RestController
public class OperateLogController {

    @Autowired
    private OperateLogService operateLogService;

    @GetMapping("/page")
    public Result page(OperateLogQueryParam operateLogQueryParam) {
        log.info("查询请求参数： {}", operateLogQueryParam);
        PageResult pageResult = operateLogService.page(operateLogQueryParam);
        return Result.success(pageResult);
    }
}
