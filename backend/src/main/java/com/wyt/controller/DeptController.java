package com.wyt.controller;

import com.wyt.anno.Log;
import com.wyt.pojo.Dept;
import com.wyt.pojo.Result;
import com.wyt.service.DeptService;
import com.wyt.validation.ValidationGroups;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    // 1. 部门查询
    @GetMapping("/depts")
    public Result list() {
        log.info("查询部门列表");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    @Log
    @PostMapping("/depts")
    public Result add(@Validated(ValidationGroups.Create.class) @RequestBody Dept dept) {
        log.info("新增部门: {}", dept.getName());
        deptService.add(dept);  
        return Result.success();
    }

    @Log
    @DeleteMapping("/depts")
    public Result delete(@RequestParam @NotNull(message = "部门ID不能为空") @Positive(message = "部门ID必须为正整数") Integer id) {
        log.info("删除部门id：{}", id);
        deptService.delete(id);
        return Result.success();
    }

    @GetMapping("/depts/{id}")
    public Result getById(@PathVariable @NotNull(message = "部门ID不能为空") @Positive(message = "部门ID必须为正整数") Integer id){
        log.info("查询部门 id={}", id);
        Dept dept =  deptService.getById(id);
        return Result.success(dept);
    }
    @Log
    @PutMapping("/depts")
    public Result update(@Validated(ValidationGroups.Update.class) @RequestBody Dept dept){
        log.info("修改部门: {}", dept);
        deptService.update(dept);
        return Result.success();
    }
}
