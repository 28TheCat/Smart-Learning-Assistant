package com.wyt.mapper;

import com.wyt.pojo.Emp;
import com.wyt.pojo.EmpExpr;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmpExprMapper {
    void insertBatch(List<EmpExpr> exprList);



    void deleteByEmpIds(@Param("empIds") List<Integer> empIds);
}
