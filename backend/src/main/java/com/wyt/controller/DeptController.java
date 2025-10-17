package com.wyt.controller;

import com.wyt.anno.Log;
import com.wyt.pojo.Dept;
import com.wyt.pojo.Result;
import com.wyt.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
    public Result add(@RequestBody Dept dept) {
        log.info("新增部门: {}", dept.getName());
        deptService.add(dept);  
        return Result.success();
    }

    @Log
    @DeleteMapping("/depts")
    public Result delete(Integer id) {
        log.info("删除部门id：{}", id);
        deptService.delete(id);  // 方法名修正
        return Result.success();
    }

    @GetMapping("/depts/{id}")
    public Result getById(@PathVariable Integer id){
        log.info(" id={}", id);
        Dept dept =  deptService.getById(id);
        return Result.success(dept);
    }
    @Log
    @PutMapping("/depts")
    public Result update(@RequestBody Dept dept){
        System.out.println("修改部门, dept=" + dept);
        deptService.update(dept);
        return Result.success();
    }
}
