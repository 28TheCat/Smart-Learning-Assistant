package com.wyt.pojo;

import lombok.Data;

@Data
public class StudentQueryParam {
    private String name;
    private Integer clazzId;
    private Integer degree;

    private Integer page = 1; //页码
    private Integer pageSize = 10; //每页展示记录数

}
