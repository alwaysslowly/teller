package com.study.teller.service;

import com.study.teller.msg.InquiryMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.InquiryReqVo;
import com.study.teller.vo.InquiryResVo;

public class InquiryService {

    public InquiryResVo inquiry(InquiryReqVo vo) throws Exception {

        // 1. 공통부 채우기
        vo.setTrCode("INQ0001");
        vo.setBankCode("012");
        vo.setBranchCode("0001");
        vo.setEmpNo("E12345");
        vo.setTrDate("20260910");
        vo.setTrTime("104800");

        // 2. 전문 만들기
        String reqMsg = InquiryMsg.pack(vo);

        // 3. 계정계 전송
        String resMsg = MsgSender.send(reqMsg);

        // 4. 응답 해석
        InquiryResVo res = InquiryMsg.unpack(resMsg);

        // 5. 응답코드 확인
        if (!"0000".equals(res.getResCode())) {
            throw new Exception("조회실패 : " + res.getResMsg());
        }

        return res;
    }
}