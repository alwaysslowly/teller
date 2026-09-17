package com.study.teller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.BizException;
import com.study.teller.common.DateUtil;
import com.study.teller.common.SessionUtil;
import com.study.teller.common.Validator;
import com.study.teller.mapper.TrHistMapper;
import com.study.teller.msg.NewAcctMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.EmpVo;
import com.study.teller.vo.NewAcctReqVo;
import com.study.teller.vo.NewAcctResVo;
import com.study.teller.vo.TrHistVo;

@Service
public class NewAcctService {

    @Autowired
    private MsgSender msgSender;

    @Autowired
    private TrHistMapper trHistMapper;

    @Autowired
    private BizDateService bizDateService;

    public NewAcctResVo newAcct(NewAcctReqVo vo) throws Exception {

        // 0. 검증
        Validator.required(vo.getCustNm(), "고객명");
        Validator.maxLength(vo.getCustNm(), 10, "고객명");

        Validator.required(vo.getProdCode(), "상품");

        Validator.required(vo.getPasswd(), "비밀번호");
        Validator.numeric(vo.getPasswd(), "비밀번호");
        Validator.maxLength(vo.getPasswd(), 4, "비밀번호");

        Validator.positive(vo.getAmount(), "초기입금액");

        // 1. 공통부
        EmpVo emp = SessionUtil.getEmp();

        vo.setTrCode("NEW0001");
        vo.setBankCode(emp.getBankCode());
        vo.setBranchCode(emp.getBranchCode());
        vo.setEmpNo(emp.getEmpNo());
        vo.setTrDate(bizDateService.getBizDate());
        vo.setTrTime(DateUtil.getNow());

        // 2~4
        String reqMsg = NewAcctMsg.pack(vo);
        String resMsg = msgSender.send(reqMsg);
        NewAcctResVo res = NewAcctMsg.unpack(resMsg);

        // 5. 이력 저장
        saveHist(vo, res);

        // 6. 응답코드 확인
        if (!"0000".equals(res.getResCode())) {
            throw new BizException(res.getResCode(), res.getResMsg());
        }

        return res;
    }


    /** 거래이력 저장 */
    private void saveHist(NewAcctReqVo vo, NewAcctResVo res) {
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
            hist.setAcctNo(res.getAcctNo());          // ★ 응답에서 받은 계좌번호
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

    private String makeTempTrNo() {
        String ms = String.valueOf(System.currentTimeMillis());
        return "F" + ms.substring(ms.length() - 11);
    }
}