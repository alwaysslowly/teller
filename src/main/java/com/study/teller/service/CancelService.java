package com.study.teller.service;

import com.study.teller.msg.CancelMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.CancelReqVo;
import com.study.teller.vo.DepositResVo;

public class CancelService {

    public DepositResVo cancel(CancelReqVo vo) throws Exception {

        // 1. 공통부 채우기
        vo.setTrCode("DEP0002");
        vo.setBankCode("012");
        vo.setBranchCode("0001");
        vo.setEmpNo("E12345");
        vo.setTrDate("20260910");
        vo.setTrTime("104800");

        // 2. 전문 만들기
        String reqMsg = CancelMsg.pack(vo);

        // 3. 계정계 전송
        String resMsg = MsgSender.send(reqMsg);

        // 4. 응답 해석
        DepositResVo res = CancelMsg.unpack(resMsg);

        // 5. 응답코드 확인
        if (!"0000".equals(res.getResCode())) {
            throw new Exception("취소실패 : " + res.getResMsg());
        }

        return res;
    }
}