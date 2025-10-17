package com.wyt.service;

import com.wyt.pojo.JobOption;
import com.wyt.pojo.StudentCountOption;

import java.util.List;
import java.util.Map;

public interface ReportService {
    JobOption getEmpJobData();

    List<Map> getEmpGenderData();

    StudentCountOption getStudentCountData();

    List<Map>   getStudentDegreeData();
}
