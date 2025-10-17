package com.wyt.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wyt.mapper.ClazzMapper;
import com.wyt.pojo.*;
import com.wyt.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;


    @Override
    public ArrayList<Clazz> findAll() {
        return clazzMapper.getInfo();
    }

    @Override
    public PageResult page(ClazzQueryParam clazz) {
        PageHelper.startPage(clazz.getPage(), clazz.getPageSize());
        List<Clazz> clazzList = clazzMapper.list(clazz);
        Page<Clazz> p = (Page<Clazz>)clazzList;
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Clazz clazz) {
        try {
            clazz.setCreateTime(LocalDateTime.now());
            clazz.setUpdateTime(LocalDateTime.now());

            clazzMapper.insert(clazz);
        }catch (Exception e) {
            // 打印异常日志
            e.printStackTrace();
            // 事务会自动回滚
            throw e;
        }
    }

    @Override
    public void delete(Integer id) {
        clazzMapper.delete(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Override
    public Clazz getById(Integer id) {
        return clazzMapper.getById(id);
    }

}

