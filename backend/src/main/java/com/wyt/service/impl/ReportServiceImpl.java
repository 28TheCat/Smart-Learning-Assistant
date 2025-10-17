package com.wyt.service.impl;

import com.wyt.mapper.EmpMapper;
import com.wyt.mapper.StudentMapper;
import com.wyt.pojo.JobOption;
import com.wyt.pojo.StudentCountOption;
import com.wyt.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private  EmpMapper empMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public JobOption getEmpJobData() {
        List<Map<String,Object>> list=empMapper.countEmpJobData();
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("total")).toList();
        return new JobOption(jobList, dataList);
    }

    @Override
    public List<Map> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }

    @Override
    public StudentCountOption  getStudentCountData() {
        List<Map<String, Object>> list = studentMapper.countStuData();
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("clazzList")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("dataList")).toList();
        return new StudentCountOption(clazzList, dataList);
    }

    @Override
    public List<Map> getStudentDegreeData() {
        return studentMapper.countStuDegreeData();
    }
}