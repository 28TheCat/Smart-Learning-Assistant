package com.wyt.mapper;

import com.wyt.pojo.Clazz;
import com.wyt.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ClazzMapper {
    List<Clazz> list(ClazzQueryParam clazzQueryParam);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into clazz(name, room, begin_date, end_date, master_id, subject, create_time, update_time) " +
    "values (#{name}, #{room}, #{beginDate}, #{endDate}, #{masterId}, #{subject}, #{createTime}, #{updateTime})")
    void insert(Clazz clazz);


    @Delete("delete from clazz where id = #{id}")
    void delete(Integer id);


    @Update("update clazz set name = #{name}, room = #{room}, begin_date = #{beginDate}, end_date = #{endDate}, master_id = #{masterId}, subject = #{subject}, update_time = #{updateTime} where id = #{id}")
    void update(Clazz clazz);


    @Select("select id, name, room, begin_date, end_date, master_id, subject, create_time, update_time from clazz where id = #{id}")
    Clazz getById(Integer id);


    @Select("select * from clazz")
    ArrayList<Clazz> getInfo();
}
