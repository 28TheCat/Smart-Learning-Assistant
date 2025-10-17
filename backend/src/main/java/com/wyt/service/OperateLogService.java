package com.wyt.service;

import com.wyt.pojo.EmpQueryParam;
import com.wyt.pojo.OperateLogQueryParam;
import com.wyt.pojo.PageResult;

public interface OperateLogService {

    PageResult page(OperateLogQueryParam operateLogQueryParam);
}
