package com.wyt.mapper;

import com.wyt.pojo.Emp;
import com.wyt.pojo.EmpQueryParam;
import com.wyt.pojo.LoginInfo;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {

    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
    Long count();

    List<Emp> list(EmpQueryParam empQueryParam);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

    void delete(List<Integer> ids);

    Emp getInfo(Integer id);

    void updateById(Emp emp);

    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();

    @MapKey("name")
    List<Map> countEmpGenderData();

    @Select("select * from emp")
    ArrayList<Emp> findAll();

    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getUsernameAndPassword(Emp emp);
}
