package com.wyt.controller;

import com.wyt.pojo.JobOption;
import com.wyt.pojo.Result;
import com.wyt.pojo.StudentCountOption;
import com.wyt.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 统计各个职位的员工人数
     */
    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("统计各个职位的员工人数");
        JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    @GetMapping("/empGenderData")
    public Result getEmpGenderData(){
        log.info("统计员工性别信息");
        List<Map> genderList = reportService.getEmpGenderData();
        return Result.success(genderList);
    }

    @GetMapping("/studentCountData")
    public Result getStudentCountData(){
        log.info("统计各个班级的学员数量");
        StudentCountOption studentCountOption = reportService.getStudentCountData();
        return Result.success(studentCountOption);
    }
    @GetMapping("/studentDegreeData")
        public Result getStudentDegreeData(){
            log.info("统计学员的学历信息");
            List<Map> degreeList = reportService.getStudentDegreeData();
            return Result.success(degreeList);
        }
}