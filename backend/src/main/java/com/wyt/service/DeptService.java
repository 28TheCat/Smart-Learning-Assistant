package com.wyt.service;

import com.wyt.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> findAll();

    void add(Dept dept);

    void delete(Integer id);

    void update(Dept dept);

    Dept getById(Integer id);
}
