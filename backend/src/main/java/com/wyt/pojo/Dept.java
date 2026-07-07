package com.wyt.pojo;

import com.wyt.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dept {
    @NotNull(groups = ValidationGroups.Update.class, message = "部门ID不能为空")
    @Positive(groups = ValidationGroups.Update.class, message = "部门ID必须为正整数")
    private Integer id;

    @NotBlank(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}, message = "部门名称不能为空")
    @Size(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}, min = 2, max = 10, message = "部门名称长度应为2到10个字符")
    private String name;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
