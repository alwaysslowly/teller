package com.study.teller.service;

import com.study.teller.common.DateUtil;
import com.study.teller.common.SessionUtil;
import com.study.teller.msg.InquiryMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.EmpVo;
import com.study.teller.vo.InquiryReqVo;
import com.study.teller.vo.InquiryResVo;

public class InquiryService {

    public InquiryResVo inquiry(InquiryReqVo vo) throws Exception {

        // 1. 공통부 채우기
        EmpVo emp = SessionUtil.getEmp();

        vo.setTrCode("INQ0001");
        vo.setBankCode(emp.getBankCode());
        vo.setBranchCode(emp.getBranchCode());
        vo.setEmpNo(emp.getEmpNo());
        vo.setTrDate(DateUtil.getToday());
        vo.setTrTime(DateUtil.getNow());
        
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