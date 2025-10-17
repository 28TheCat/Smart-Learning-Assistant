package com.wyt.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperateLogQueryParam {
    private Integer page = 1;           // 页码
    private Integer pageSize = 10;      // 每页展示记录数
    private Integer operateEmpId;       // 操作员工ID
    private String operateEmpName;
    private String methodName;          // 方法名
    private LocalDateTime beginTime;    // 开始时间
    private LocalDateTime endTime;// 结束时间
}