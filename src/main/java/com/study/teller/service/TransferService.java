package com.study.teller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.BizException;
import com.study.teller.common.DateUtil;
import com.study.teller.common.SessionUtil;
import com.study.teller.common.Validator;
import com.study.teller.mapper.TrHistMapper;
import com.study.teller.msg.TransferMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.EmpVo;
import com.study.teller.vo.TrHistVo;
import com.study.teller.vo.TransferReqVo;
import com.study.teller.vo.TransferResVo;

@Service
public class TransferService {

    @Autowired
    private MsgSender msgSender;

    @Autowired
    private TrHistMapper trHistMapper;

    @Autowired
    private BizDateService bizDateService;

    public TransferResVo transfer(TransferReqVo vo) throws Exception {

        // 0. 검증
        Validator.required(vo.getOutAcctNo(), "출금계좌");
        Validator.numeric(vo.getOutAcctNo(), "출금계좌");
        Validator.maxLength(vo.getOutAcctNo(), 14, "출금계좌");

        Validator.required(vo.getPasswd(), "비밀번호");
        Validator.numeric(vo.getPasswd(), "비밀번호");
        Validator.maxLength(vo.getPasswd(), 4, "비밀번호");

        Validator.required(vo.getInAcctNo(), "입금계좌");
        Validator.numeric(vo.getInAcctNo(), "입금계좌");
        Validator.maxLength(vo.getInAcctNo(), 14, "입금계좌");

        Validator.positive(vo.getAmount(), "이체금액");

        if (vo.getOutAcctNo().equals(vo.getInAcctNo())) {
            throw new BizException("V007", "출금계좌와 입금계좌가 같습니다.");
        }

        // 1. 공통부
        EmpVo emp = SessionUtil.getEmp();

        vo.setTrCode("TRF0001");
        vo.setBankCode(emp.getBankCode());
        vo.setBranchCode(emp.getBranchCode());
        vo.setEmpNo(emp.getEmpNo());
        vo.setTrDate(bizDateService.getBizDate());
        vo.setTrTime(DateUtil.getNow());

        // 2~4
        String reqMsg = TransferMsg.pack(vo);
        String resMsg = msgSender.send(reqMsg);
        TransferResVo res = TransferMsg.unpack(resMsg);

        // 5. 이력 저장 (출금/입금 두 건)
        saveHist(vo, res);

        // 6. 응답코드 확인
        if (!"0000".equals(res.getResCode())) {
            throw new BizException(res.getResCode(), res.getResMsg());
        }

        return res;
    }


    /** 이체 이력 저장 (출금 + 입금 두 건) */
    private void saveHist(TransferReqVo vo, TransferResVo res) {
        try {
            boolean ok = "0000".equals(res.getResCode());
            long amount = Long.parseLong(vo.getAmount());
            String trNo = res.getTrNo() == null || res.getTrNo().trim().isEmpty()
                          ? makeTempTrNo() : res.getTrNo().trim();

            // 출금 이력
            TrHistVo outHist = newHist(vo, res, trNo);
            outHist.setAcctNo(vo.getOutAcctNo());
            outHist.setAmount(-amount);
            outHist.setBalance(toLong(res.getBalance()));
            trHistMapper.insertHist(outHist);

            // 입금 이력 (성공한 경우만)
            if (ok) {
                TrHistVo inHist = newHist(vo, res, trNo + "I");
                inHist.setAcctNo(vo.getInAcctNo());
                inHist.setCustNm(res.getInCustNm());
                inHist.setAmount(amount);
                inHist.setBalance(0);
                trHistMapper.insertHist(inHist);
            }

        } catch (Exception e) {
            System.out.println("[이력저장 실패] " + e.getMessage());
        }
    }


    private TrHistVo newHist(TransferReqVo vo, TransferResVo res, String trNo) {
        TrHistVo h = new TrHistVo();
        h.setTrNo(trNo);
        h.setBankCode(vo.getBankCode());
        h.setBranchCode(vo.getBranchCode());
        h.setEmpNo(vo.getEmpNo());
        h.setTrCode(vo.getTrCode());
        h.setTrDate(vo.getTrDate());
        h.setTrTime(vo.getTrTime());
        h.setCustNm("");
        h.setResCode(res.getResCode());
        h.setResMsg(res.getResMsg());
        return h;
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