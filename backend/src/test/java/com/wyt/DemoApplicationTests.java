package com.wyt;

import com.wyt.mapper.EmpMapper;
import com.wyt.pojo.Emp;
import com.wyt.pojo.EmpQueryParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private EmpMapper empMapper;

    @Test
    public void testList(EmpQueryParam empQueryParam){
        List<Emp> empList = empMapper.list(empQueryParam);
        empList.forEach(System.out::println);
    }

}
