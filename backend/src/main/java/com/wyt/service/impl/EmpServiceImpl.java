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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

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
            encodePasswordIfPresent(emp);

            // 插入员工主表
            empMapper.insert(emp);

            // 插入员工工作经历
            List<EmpExpr> exprList = emp.getExprList();
            if (!CollectionUtils.isEmpty(exprList)) {
                exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
                empExprMapper.insertBatch(exprList);
            }

        } catch (Exception e) {
            log.error("保存员工失败 empId={}, username={}", emp.getId(), emp.getUsername(), e);
            throw e;
        } finally {
            // 记录操作日志，无论事务是否成功；避免把密码或 hash 写入日志。
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), sanitizeEmpForLog(emp));
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
        encodePasswordIfPresent(emp);
        String encodedPassword = emp.getPassword();
        emp.setPassword(null);

        empMapper.updateById(emp);
        if (StringUtils.hasText(encodedPassword)) {
            empMapper.updatePasswordById(emp.getId(), encodedPassword, emp.getUpdateTime());
        }

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
    public LoginInfo login(LoginParam loginParam) {
        Emp empLogin = empMapper.getByUsername(loginParam.getUsername());
        if (empLogin == null || !passwordMatches(loginParam.getPassword(), empLogin.getPassword())) {
            return null;
        }

        upgradePlaintextPasswordIfNeeded(empLogin, loginParam.getPassword());

        Map<String,Object> claims= new HashMap<>();
        claims.put("id", empLogin.getId());
        claims.put("username", empLogin.getUsername());

        String jwt = jwtUtils.generateJwt(claims);
        return new LoginInfo(empLogin.getId(), empLogin.getUsername(), empLogin.getName(), jwt);
    }

    private void encodePasswordIfPresent(Emp emp) {
        if (StringUtils.hasText(emp.getPassword()) && !isBcryptHash(emp.getPassword())) {
            emp.setPassword(passwordEncoder.encode(emp.getPassword()));
        }
    }

    private boolean passwordMatches(String rawPassword, String storedPassword) {
        if (!StringUtils.hasText(rawPassword) || !StringUtils.hasText(storedPassword)) {
            return false;
        }

        if (isBcryptHash(storedPassword)) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        return rawPassword.equals(storedPassword);
    }

    private void upgradePlaintextPasswordIfNeeded(Emp emp, String rawPassword) {
        if (!isBcryptHash(emp.getPassword())) {
            empMapper.updatePasswordById(emp.getId(), passwordEncoder.encode(rawPassword), LocalDateTime.now());
        }
    }

    private boolean isBcryptHash(String password) {
        return password != null && password.matches("^\\$2[aby]\\$.{56}$");
    }

    private String sanitizeEmpForLog(Emp emp) {
        if (emp == null) {
            return "null";
        }
        return "Emp(id=" + emp.getId() + ", username=" + emp.getUsername() + ", name=" + emp.getName() + ")";
    }

}
