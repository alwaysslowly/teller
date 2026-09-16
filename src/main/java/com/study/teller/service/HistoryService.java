package com.study.teller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.BizException;
import com.study.teller.common.DateUtil;
import com.study.teller.common.SessionUtil;
import com.study.teller.common.Validator;
import com.study.teller.msg.HistoryMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.EmpVo;
import com.study.teller.vo.HistoryReqVo;
import com.study.teller.vo.HistoryResVo;

@Service
public class HistoryService {
	
    @Autowired
    private MsgSender msgSender;
    @Autowired
    private BizDateService bizDateService;   


    public HistoryResVo history(HistoryReqVo vo) throws Exception {
    	
    	  // 0. 입력값 검증
        Validator.required(vo.getAcctNo(), "계좌번호");
        Validator.numeric(vo.getAcctNo(), "계좌번호");
        Validator.date(vo.getFromDate(), "조회시작일");
        Validator.date(vo.getToDate(), "조회종료일");

        if (vo.getFromDate().compareTo(vo.getToDate()) > 0) {
            throw new BizException("V006", "조회 시작일이 종료일보다 늦습니다.");
        }

        // 1. 공통부 채우기
        EmpVo emp = SessionUtil.getEmp();

        vo.setTrCode("INQ0002");
        vo.setBankCode(emp.getBankCode());
        vo.setBranchCode(emp.getBranchCode());
        vo.setEmpNo(emp.getEmpNo());
        vo.setTrDate(bizDateService.getBizDate());
        vo.setTrTime(DateUtil.getNow());
        
 
        // 2. 전문 만들기
        String reqMsg = HistoryMsg.pack(vo);

        // 3. 계정계 전송
        String resMsg = msgSender.send(reqMsg);

        // 4. 응답 해석
        HistoryResVo res = HistoryMsg.unpack(resMsg);

        // 5. 응답코드 확인
        if (!"0000".equals(res.getResCode())) {
        	throw new BizException(res.getResCode(), res.getResMsg());
        }

        return res;
    }
}