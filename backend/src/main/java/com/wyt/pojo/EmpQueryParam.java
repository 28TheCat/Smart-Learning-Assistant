package com.wyt.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpQueryParam {

    private Integer page = 1; //页码
    private Integer pageSize = 10; //每页展示记录数
    private String name; //姓名
    private Integer gender; //性别

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin; //开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end; //入职结束时间

}
