package com.study.teller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.teller.common.BizException;
import com.study.teller.common.DateUtil;
import com.study.teller.common.SessionUtil;
import com.study.teller.common.Validator;
import com.study.teller.mapper.TrHistMapper;
import com.study.teller.msg.CloseMsg;
import com.study.teller.sender.MsgSender;
import com.study.teller.vo.CloseReqVo;
import com.study.teller.vo.DepositResVo;
import com.study.teller.vo.EmpVo;
import com.study.teller.vo.TrHistVo;

@Service
public class CloseService {

    @Autowired
    private MsgSender msgSender;

    @Autowired
    private TrHistMapper trHistMapper;

    @Autowired
    private BizDateService bizDateService;


    /** 계좌해지 */
    public DepositResVo close(CloseReqVo vo) throws Exception {

        Validator.required(vo.getAcctNo(), "계좌번호");
        Validator.numeric(vo.getAcctNo(), "계좌번호");
        Validator.required(vo.getPasswd(), "비밀번호");
        Validator.numeric(vo.getPasswd(), "비밀번호");
        Validator.maxLength(vo.getPasswd(), 4, "비밀번호");

        setHeader(vo, "CLS0001");

        String reqMsg = CloseMsg.packClose(vo);
        String resMsg = msgSender.send(reqMsg);
        DepositResVo res = CloseMsg.unpack(resMsg);

        saveHist(vo, res, false);

        if (!"0000".equals(res.getResCode())) {
            throw new BizException(res.getResCode(), res.getResMsg());
        }
        return res;
    }


    /** 해지취소 */
    public DepositResVo cancelClose(CloseReqVo vo) throws Exception {

        // 책임자 권한
        SessionUtil.checkManager();

        Validator.required(vo.getAcctNo(), "계좌번호");
        Validator.required(vo.getOrgTrNo(), "원거래번호");

        setHeader(vo, "CLS0002");

        String reqMsg = CloseMsg.packCancel(vo);
        String resMsg = msgSender.send(reqMsg);
        DepositResVo res = CloseMsg.unpack(resMsg);

        saveHist(vo, res, true);

        if (!"0000".equals(res.getResCode())) {
            throw new BizException(res.getResCode(), res.getResMsg());
        }
        return res;
    }


    /** 공통부 */
    private void setHeader(CloseReqVo vo, String trCode) {
        EmpVo emp = SessionUtil.getEmp();
        vo.setTrCode(trCode);
        vo.setBankCode(emp.getBankCode());
        vo.setBranchCode(emp.getBranchCode());
        vo.setEmpNo(emp.getEmpNo());
        vo.setTrDate(bizDateService.getBizDate());
        vo.setTrTime(DateUtil.getNow());
    }


    /** 이력 저장 */
    private void saveHist(CloseReqVo vo, DepositResVo res, boolean isCancel) {
        try {
            long amt = toLong(res.getBalance());

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
            hist.setAmount(isCancel ? amt : -amt);    // 해지는 지급(-), 취소는 복구(+)
            hist.setBalance(isCancel ? amt : 0);
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