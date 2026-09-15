package com.study.teller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.BizException;
import com.study.teller.common.Validator;
import com.study.teller.mapper.TrHistMapper;
import com.study.teller.msg.CancelMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.CancelReqVo;
import com.study.teller.vo.DepositResVo;
import com.study.teller.vo.TrHistVo;

@Service
public class CancelService {

    @Autowired
    private TrHistMapper trHistMapper;

    public DepositResVo cancel(CancelReqVo vo) throws Exception {

        // 0. 검증
        Validator.required(vo.getOrgTrNo(), "원거래번호");
        Validator.required(vo.getCancelRsn(), "취소사유");

        // 1. 공통부
        vo.setTrCode("DEP0002");
        vo.setBankCode("012");
        vo.setBranchCode("0001");
        vo.setEmpNo("E12345");
        vo.setTrDate("20260910");
        vo.setTrTime("104800");

        // 2~4
        String reqMsg = CancelMsg.pack(vo);
        String resMsg = MsgSender.send(reqMsg);
        DepositResVo res = CancelMsg.unpack(resMsg);

        // 5. 이력 저장
        saveHist(vo, res);

        // 6. 응답코드 확인
        if (!"0000".equals(res.getResCode())) {
            throw new BizException(res.getResCode(), res.getResMsg());
        }

        return res;
    }

    /** 취소 이력 저장 */
    private void saveHist(CancelReqVo vo, DepositResVo res) {
        try {
            // 원거래 조회
            TrHistVo org = trHistMapper.selectHist(vo.getOrgTrNo());

            TrHistVo hist = new TrHistVo();
            hist.setTrNo(res.getTrNo() == null || res.getTrNo().trim().isEmpty()
                         ? makeTempTrNo() : res.getTrNo().trim());
            hist.setBankCode(vo.getBankCode());
            hist.setBranchCode(vo.getBranchCode());
            hist.setEmpNo(vo.getEmpNo());
            hist.setTrCode(vo.getTrCode());
            hist.setTrDate(vo.getTrDate());
            hist.setTrTime(vo.getTrTime());

            if (org != null) {
                hist.setAcctNo(org.getAcctNo());
                hist.setCustNm(org.getCustNm());
                hist.setAmount(-org.getAmount());     // ★ 음수
            }

            hist.setBalance(toLong(res.getBalance()));
            hist.setResCode(res.getResCode());
            hist.setResMsg(res.getResMsg());

            trHistMapper.insertHist(hist);

        } catch (Exception e) {
            System.out.println("[이력저장 실패] " + e.getMessage());
        }
    }

    private long toLong(String s) {
        if (s == null || s.trim().isEmpty()) return 0;
        return Long.parseLong(s.trim());
    }

    private String makeTempTrNo() {
        String ms = String.valueOf(System.currentTimeMillis());
        return "F" + ms.substring(ms.length() - 11);
    }
}