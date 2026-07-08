package com.wyt.mapper;

import com.wyt.pojo.Emp;
import com.wyt.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {

    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
    Long count();

    List<Emp> list(EmpQueryParam empQueryParam);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp(username, password, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "values (#{username},#{password},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
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

    @Select("select * from emp where username = #{username}")
    Emp getByUsername(String username);

    @Update("update emp set password = #{password}, update_time = #{updateTime} where id = #{id}")
    void updatePasswordById(@Param("id") Integer id, @Param("password") String password, @Param("updateTime") LocalDateTime updateTime);
}
