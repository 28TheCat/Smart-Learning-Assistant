package com.wyt.service;

import com.wyt.pojo.PageResult;
import com.wyt.pojo.Student;
import com.wyt.pojo.StudentQueryParam;

import java.util.List;

public interface StudentService {
    PageResult page(StudentQueryParam studentQueryParam);

    Student getById(Integer id);

    void save(Student student);

    void delete(List<Integer> ids);

    void update(Student student);

    void updateScore(Integer id, Short score);
}
