package com.wyt;

import com.wyt.pojo.EmpQueryParam;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DemoApplicationTests {

    @Test
    void empQueryParamUsesDefaultPagination() {
        EmpQueryParam empQueryParam = new EmpQueryParam();

        assertThat(empQueryParam.getPage()).isEqualTo(1);
        assertThat(empQueryParam.getPageSize()).isEqualTo(10);
    }

    @Test
    void empQueryParamKeepsSearchCriteria() {
        EmpQueryParam empQueryParam = new EmpQueryParam();
        LocalDate begin = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 12, 31);

        empQueryParam.setName("admin");
        empQueryParam.setGender(1);
        empQueryParam.setBegin(begin);
        empQueryParam.setEnd(end);

        assertThat(empQueryParam.getName()).isEqualTo("admin");
        assertThat(empQueryParam.getGender()).isEqualTo(1);
        assertThat(empQueryParam.getBegin()).isEqualTo(begin);
        assertThat(empQueryParam.getEnd()).isEqualTo(end);
    }

}
