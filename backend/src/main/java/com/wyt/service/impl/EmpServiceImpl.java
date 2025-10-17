package com.wyt.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wyt.Utils.JwtUtils;
import com.wyt.mapper.EmpExprMapper;
import com.wyt.mapper.EmpMapper;
import com.wyt.pojo.*;
import com.wyt.service.EmpLogService;
import com.wyt.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;
    public PageResult page(EmpQueryParam empQueryParam) {
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        List<Emp> empList = empMapper.list(empQueryParam);
        Page<Emp> p = (Page<Emp>)empList;
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void save(Emp emp) {
        try {
            // 设置创建/更新时间
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());

            // 插入员工主表
            empMapper.insert(emp);

            // 插入员工工作经历
            List<EmpExpr> exprList = emp.getExprList();
            if (!CollectionUtils.isEmpty(exprList)) {
                exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
                empExprMapper.insertBatch(exprList);
            }

        } catch (Exception e) {
            // 打印异常日志
            e.printStackTrace();
            // 事务会自动回滚
            throw e;
        } finally {
            // 记录操作日志，无论事务是否成功
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), emp.toString());
            empLogService.insertLog(empLog);
        }
    }

    @Override
    public void delete(List<Integer> ids) {
        empMapper.delete(ids);
        empExprMapper.deleteByEmpIds(ids);
    }

    @Override
    public Emp getInfo(Integer id) {
        return  empMapper.getInfo(id);
    }

    @Transactional
    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());

        empMapper.updateById(emp);

        empExprMapper.deleteByEmpIds(Collections.singletonList(emp.getId()));

        Integer empId = emp.getId();
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> empExpr.setEmpId(empId));
            empExprMapper.insertBatch(exprList);
        }
    }

    @Override
    public ArrayList<Emp> findAll() {
        return empMapper.findAll();
    }

    @Override
    public LoginInfo login(Emp emp) {
        Emp empLogin = empMapper.getUsernameAndPassword(emp);
        if(empLogin != null){
            Map<String,Object> claims= new HashMap<>();
            claims.put("id", empLogin.getId());
            claims.put("username", empLogin.getUsername());

            String jwt = JwtUtils.generateJwt(claims );
            LoginInfo loginInfo = new LoginInfo(empLogin.getId(), empLogin.getUsername(), empLogin.getName(), jwt);
            return loginInfo;
        }
        return null;
    }


}
