package com.wyt.service;

import com.wyt.pojo.Clazz;
import com.wyt.pojo.ClazzQueryParam;
import com.wyt.pojo.PageResult;

import java.util.ArrayList;

public interface ClazzService {

    ArrayList<Clazz> findAll() ;

    PageResult page(ClazzQueryParam clazz);

    void save(Clazz clazz);

    void delete(Integer id);

    void update(Clazz clazz);

    Clazz getById(Integer id);
}
