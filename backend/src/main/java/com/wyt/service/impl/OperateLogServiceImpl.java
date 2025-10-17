package com.wyt.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wyt.mapper.OperateLogMapper;
import com.wyt.pojo.*;
import com.wyt.service.OperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {
    @Autowired
    private OperateLogMapper operateLogMapper;
    @Override
    public PageResult page(OperateLogQueryParam operateLogQueryParam) {
        PageHelper.startPage(operateLogQueryParam.getPage(), operateLogQueryParam.getPageSize());
        List<Emp> OperateLogList = operateLogMapper.list(operateLogQueryParam);
        Page<Emp> p = (Page<Emp>)OperateLogList;
        return new PageResult(p.getTotal(), p.getResult());
    }
}
