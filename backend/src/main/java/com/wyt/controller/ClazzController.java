package com.wyt.controller;

import com.wyt.anno.Log;
import com.wyt.pojo.*;
import com.wyt.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam) {
        log.info("查询所有班级列表 {}", clazzQueryParam);
        PageResult pageResult= clazzService.page(clazzQueryParam);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询班级：{}", id);
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }

    @GetMapping("/list")
    public Result findAll() {
        log.info("查询所有员工列表");
        ArrayList<Clazz> clazzes = clazzService.findAll();
        return Result.success(clazzes);
    }

    @PostMapping()
    @Log
    public Result save(@RequestBody Clazz clazz) {
        log.info("新增班级：{}", clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除班级：{}", id);
        clazzService.delete(id);
        return Result.success();
    }
    @Log
    @PutMapping()
    public Result update(@RequestBody Clazz clazz){
        System.out.println("修改班级, clazz=" + clazz);
        clazzService.update(clazz);
        return Result.success();
    }

}
