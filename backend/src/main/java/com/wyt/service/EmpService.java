package com.wyt.service;

import com.wyt.pojo.Emp;
import com.wyt.pojo.EmpQueryParam;
import com.wyt.pojo.LoginInfo;
import com.wyt.pojo.PageResult;

import java.util.ArrayList;
import java.util.List;


public interface EmpService {

    PageResult<Emp> page(EmpQueryParam empQueryParam);

    void save(Emp emp);

    void delete(List<Integer> ids);

    Emp getInfo(Integer id);

    void update(Emp emp);

    ArrayList<Emp> findAll();

    LoginInfo login(Emp emp);
}