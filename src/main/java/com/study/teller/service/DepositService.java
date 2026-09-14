package com.study.teller.service;

import com.study.teller.common.BizException;
import com.study.teller.common.MsgUtil;
import com.study.teller.common.Validator;
import com.study.teller.msg.DepositMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.DepositReqVo;
import com.study.teller.vo.DepositResVo;

public class DepositService {

    public DepositResVo deposit(DepositReqVo vo) throws Exception {
    	
        // 0. 입력값 검증
        Validator.required(vo.getAcctNo(), "계좌번호");
        Validator.numeric(vo.getAcctNo(), "계좌번호");
        Validator.maxLength(vo.getAcctNo(), 14, "계좌번호");

        Validator.required(vo.getCustNm(), "고객명");
        Validator.maxLength(vo.getCustNm(), 10, "고객명");

        Validator.positive(vo.getAmount(), "입금금액");

        // 1. 공통부 채우기
        vo.setTrCode("DEP0001");
        vo.setBankCode("012");
        vo.setBranchCode("0001");
        vo.setEmpNo("E12345");
        vo.setTrDate("20260910");
        vo.setTrTime("104800");

        // 2. 전문 만들기
        String reqMsg = DepositMsg.pack(vo);

        // 3. 계정계 전송
        String resMsg = MsgSender.send(reqMsg);

        // 4. 응답 해석
        DepositResVo res = DepositMsg.unpack(resMsg);

        // 5. 응답코드 확인
        // 5. 응답코드 확인
        if (!"0000".equals(res.getResCode())) {
            throw new BizException(res.getResCode(), res.getResMsg());
        }

        return res;
    }
}