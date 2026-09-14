package com.study.teller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.BizException;
import com.study.teller.common.Validator;
import com.study.teller.mapper.TrHistMapper;
import com.study.teller.msg.DepositMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.DepositReqVo;
import com.study.teller.vo.DepositResVo;
import com.study.teller.vo.TrHistVo;

@Service
public class DepositService {

    @Autowired
    private TrHistMapper trHistMapper;

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

        // 5. 이력 저장 (성공/실패 모두)
        saveHist(vo, res);

        // 6. 응답코드 확인
        if (!"0000".equals(res.getResCode())) {
            throw new BizException(res.getResCode(), res.getResMsg());
        }

        return res;
    }

    /** 거래이력 저장 */
    private void saveHist(DepositReqVo vo, DepositResVo res) {
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
            hist.setCustNm(vo.getCustNm());
            hist.setAmount(Long.parseLong(vo.getAmount()));
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
    
    /** 실패 거래용 임시 거래번호 (12자리) */
    private String makeTempTrNo() {
        String ms = String.valueOf(System.currentTimeMillis());
        return "F" + ms.substring(ms.length() - 11);
    }
    
    
}