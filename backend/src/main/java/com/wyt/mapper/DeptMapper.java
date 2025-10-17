package com.wyt.mapper;

import com.wyt.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {

    // 查询所有的部门数据
    @Select("select id, name, create_time, update_time from dept order by update_time desc")
    List<Dept> findAll();

    // 新增部门 - 简化版本（让数据库处理时间）
    @Insert("insert into dept(name, create_time, update_time) values(#{name}, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Dept dept);

    // 删除部门
    @Delete("delete from dept where id = #{id}")
    void delete(Integer id);

    @Update("update dept set name = #{name},update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);

    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);
}