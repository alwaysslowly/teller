package com.study.teller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.BizException;
import com.study.teller.common.Validator;
import com.study.teller.mapper.EmpMapper;
import com.study.teller.vo.EmpVo;

@Service
public class LoginService {

    @Autowired
    private EmpMapper empMapper;

    public EmpVo login(String empNo, String passwd) {

        Validator.required(empNo, "직원번호");
        Validator.required(passwd, "비밀번호");

        EmpVo emp = empMapper.selectEmp(empNo);

        // 없는 직원
        if (emp == null) {
            throw new BizException("L001", "직원번호 또는 비밀번호가 올바르지 않습니다.");
        }

        // 사용중지 계정
        if (!"Y".equals(emp.getUseYn())) {
            throw new BizException("L002", "사용할 수 없는 계정입니다.");
        }

        // 비밀번호 확인
        if (!passwd.equals(emp.getPasswd())) {
            throw new BizException("L001", "직원번호 또는 비밀번호가 올바르지 않습니다.");
        }

        emp.setPasswd(null);      // ★ 비밀번호는 밖으로 내보내지 않는다
        return emp;
    }
}