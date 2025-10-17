package com.wyt.mapper;


import com.wyt.pojo.Emp;
import com.wyt.pojo.OperateLog;
import com.wyt.pojo.OperateLogQueryParam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OperateLogMapper {

    //插入日志数据
    @Insert("insert into operate_log (operate_emp_id, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "values (#{operateEmpId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime});")
    public void insert(OperateLog log);


    // 查询日志总数
    @Select("SELECT COUNT(*) FROM operate_log")
    int countLogs();

    // 分页查询
    @Select("SELECT * FROM operate_log ORDER BY operate_time DESC LIMIT #{limit} OFFSET #{offset}")
    List<OperateLog> selectLogs(@Param("offset") int offset, @Param("limit") int limit);

    List<Emp> list(OperateLogQueryParam operateLogQueryParam);
}