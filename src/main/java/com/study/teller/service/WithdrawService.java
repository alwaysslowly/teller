package com.study.teller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.BizException;
import com.study.teller.common.DateUtil;
import com.study.teller.common.SessionUtil;
import com.study.teller.common.Validator;
import com.study.teller.mapper.TrHistMapper;
import com.study.teller.msg.WithdrawMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.DepositResVo;
import com.study.teller.vo.EmpVo;
import com.study.teller.vo.TrHistVo;
import com.study.teller.vo.WithdrawReqVo;

@Service
public class WithdrawService {
    @Autowired
    private MsgSender msgSender;

    @Autowired
    private TrHistMapper trHistMapper;

    @Autowired
    private BizDateService bizDateService;

    public DepositResVo withdraw(WithdrawReqVo vo) throws Exception {

        // 0. 입력값 검증
        Validator.required(vo.getAcctNo(), "계좌번호");
        Validator.numeric(vo.getAcctNo(), "계좌번호");
        Validator.maxLength(vo.getAcctNo(), 14, "계좌번호");

        Validator.required(vo.getPasswd(), "비밀번호");
        Validator.numeric(vo.getPasswd(), "비밀번호");
        Validator.maxLength(vo.getPasswd(), 4, "비밀번호");

        Validator.positive(vo.getAmount(), "출금금액");

        // 1. 공통부
        EmpVo emp = SessionUtil.getEmp();

        vo.setTrCode("WTD0001");
        vo.setBankCode(emp.getBankCode());
        vo.setBranchCode(emp.getBranchCode());
        vo.setEmpNo(emp.getEmpNo());
        vo.setTrDate(bizDateService.getBizDate());
        vo.setTrTime(DateUtil.getNow());

        // 2~4
        String reqMsg = WithdrawMsg.pack(vo);
        String resMsg = msgSender.send(reqMsg);
        DepositResVo res = WithdrawMsg.unpack(resMsg);

        // 5. 이력 저장
        saveHist(vo, res);

        // 6. 응답코드 확인
        if (!"0000".equals(res.getResCode())) {
            throw new BizException(res.getResCode(), res.getResMsg());
        }

        return res;
    }

    /** 거래이력 저장 */
    private void saveHist(WithdrawReqVo vo, DepositResVo res) {
        try {
            TrHistVo hist = new TrHistVo();
            hist.setTrNo(res.getTrNo() == null || res.getTrNo().trim().isEmpty()
                         ? makeTempTrNo() : res.getTrNo().trim());
            hist.setBankCode(vo.getBankCode());
            hist.setBranchCode(vo.getBranchCode());
            hist.setEmpNo(vo.getEmpNo());
            hist.setTrCode(vo.getTrCode());
            hist.setTrDate(vo.getTrDate());
            hist.setTrTime(vo.getTrTime());
            hist.setAcctNo(vo.getAcctNo());
            hist.setCustNm("");
            hist.setAmount(-Long.parseLong(vo.getAmount()));   // ★ 출금은 음수
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