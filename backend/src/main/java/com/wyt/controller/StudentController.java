package com.wyt.controller;

import com.wyt.anno.Log;
import com.wyt.mapper.StudentMapper;
import com.wyt.pojo.PageResult;
import com.wyt.pojo.Result;
import com.wyt.pojo.Student;
import com.wyt.pojo.StudentQueryParam;
import com.wyt.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentMapper studentMapper;

    @GetMapping
    public Result page(StudentQueryParam studentQueryParam){
        log.info("studentQueryParam查询请求参数： {}", studentQueryParam);
        PageResult pageResult = studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info(" 查询学生，id={}", id);
        Student student =  studentService.getById(id);
        return Result.success(student);
    }
    @Log
    @PostMapping
    public Result save(@RequestBody Student student){
        log.info("保存学生信息：{}", student);
        studentService.save(student);
        return Result.success();
    }
    @Log
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        log.info("删除学生，id={}", ids);
        studentService.delete(ids);
        return Result.success();
    }
    @Log
    @PutMapping
    public Result update(@RequestBody Student student){
        log.info("更新学生信息：{}", student);
        studentService.update(student);
        return Result.success();
    }
    @Log
    @PutMapping("/violation/{id}/{score}")
    public Result updateScore(@PathVariable Integer id, @PathVariable Short score){
        log.info("更新学生状态，id={}，status={}", id, score);
        studentService.updateScore(id, score);
        return Result.success();
    }

}
