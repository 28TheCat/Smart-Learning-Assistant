package com.wyt.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wyt.mapper.StudentMapper;
import com.wyt.pojo.PageResult;
import com.wyt.pojo.Student;
import com.wyt.pojo.StudentQueryParam;
import com.wyt.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServicelmpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult page(StudentQueryParam studentQueryParam) {
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        List<Student> studentList = studentMapper.list(studentQueryParam);
        Page<Student> p = (Page<Student>)studentList;
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public Student getById(Integer id) {
        return studentMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        student.setCreateTime(LocalDateTime.now());
        studentMapper.save(student);
    }

    @Override
    public void delete(List<Integer> ids) {
        studentMapper.delete(ids);
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    public void updateScore(Integer id, Short score) {
        Student student = studentMapper.getById(id);
        student.setUpdateTime(LocalDateTime.now());
        student.setViolationScore((short) (student.getViolationScore()+score));
        student.setViolationCount((short) (student.getViolationCount()+1));
        studentMapper.updateScore(student);
    }
}
