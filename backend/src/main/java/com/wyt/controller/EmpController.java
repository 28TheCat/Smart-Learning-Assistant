package com.wyt.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wyt.anno.Log;
import com.wyt.mapper.EmpMapper;
import com.wyt.pojo.Emp;
import com.wyt.pojo.EmpQueryParam;
import com.wyt.pojo.PageResult;
import com.wyt.pojo.Result;
import com.wyt.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequestMapping("/emps")
@RestController
public class EmpController {
    @Autowired
    private EmpService empService;
    @Autowired
    private EmpMapper empMapper;

    @GetMapping("/list")
    public Result findAll() {
        log.info("查询所有员工列表");
        ArrayList<Emp> emps = empService.findAll();
        return Result.success(emps);
    }

    @GetMapping
    public Result page(EmpQueryParam empQueryParam) {
        log.info("查询请求参数： {}", empQueryParam);
        PageResult pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }
    @Log
    @PostMapping
    public Result save(@RequestBody Emp emp){
        log.info("新增员工：{}",emp);
        empService.save(emp);
        return Result.success();
    }
    @Log
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
        log.info("删除员工：{}", ids);
        empService.delete(ids);
        return Result.success();
    }



    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        log.info("根据id查询员工的详细信息");
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }
    @Log
    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("修改员工信息：{}", emp);
        empService.update(emp);
        return Result.success();
    }
}
