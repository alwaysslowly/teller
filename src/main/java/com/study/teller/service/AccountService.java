package com.study.teller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.SessionUtil;
import com.study.teller.mapper.AccountMapper;
import com.study.teller.vo.AccountVo;
import com.study.teller.vo.EmpVo;

@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;

    public List<AccountVo> getAccountList() {

        EmpVo emp = SessionUtil.getEmp();
        return accountMapper.selectAccountList(emp.getBankCode());
    }
}