package com.wyt.mapper;

import com.wyt.pojo.Student;
import com.wyt.pojo.StudentCountOption;
import com.wyt.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    List<Student> list(StudentQueryParam studentQueryParam);


    @Select("select * from student where id = #{id}")
    Student getById(Integer id);

     void save(Student student);

    void delete(List<Integer> ids);


    void update(Student student);

    void updateScore(Student student);


    List<Map<String, Object>> countStuData();


    List<Map> countStuDegreeData();
}
