package com.study.teller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.BizException;
import com.study.teller.common.DateUtil;
import com.study.teller.common.SessionUtil;
import com.study.teller.mapper.CashMapper;
import com.study.teller.vo.CashVo;
import com.study.teller.vo.EmpVo;

@Service
public class CashService {

    @Autowired
    private CashMapper cashMapper;

    /** 시재 현황 조회 */
    public CashVo getCash() {

        EmpVo emp = SessionUtil.getEmp();

        CashVo param = new CashVo();
        param.setBaseDate(DateUtil.getToday());
        param.setBranchCode(emp.getBranchCode());
        param.setEmpNo(emp.getEmpNo());

        // 1. 시재 정보
        CashVo cash = cashMapper.selectCash(param);
        if (cash == null) {
            throw new BizException("C001", "오늘 시재 정보가 없습니다. 관리자에게 문의하세요.");
        }

        // 2. 당일 거래 집계
        CashVo sum = cashMapper.selectTrSum(param);

        cash.setInAmt(sum.getInAmt());
        cash.setOutAmt(sum.getOutAmt());

        // 3. 장부상 현금 = 시작시재 + 입금 - 출금
        long bookAmt = cash.getStartAmt() + sum.getInAmt() - sum.getOutAmt();
        cash.setBookAmt(bookAmt);

        return cash;
    }


    /** 마감 처리 */
    public CashVo close(long realAmt) {

        EmpVo emp = SessionUtil.getEmp();

        // 1. 현재 상태 확인
        CashVo cash = getCash();

        if ("Y".equals(cash.getCloseYn())) {
            throw new BizException("C002", "이미 마감되었습니다.");
        }

        // 2. 차액 확인
        long diff = realAmt - cash.getBookAmt();
        if (diff != 0) {
            throw new BizException("C003",
                "실제 현금과 장부가 맞지 않습니다. (차액 " + diff + "원)");
        }

        // 3. 마감 처리
        CashVo param = new CashVo();
        param.setBaseDate(DateUtil.getToday());
        param.setBranchCode(emp.getBranchCode());
        param.setEmpNo(emp.getEmpNo());
        param.setRealAmt(realAmt);

        cashMapper.updateClose(param);

        cash.setRealAmt(realAmt);
        cash.setCloseYn("Y");
        cash.setDiffAmt(0);

        return cash;
    }
}