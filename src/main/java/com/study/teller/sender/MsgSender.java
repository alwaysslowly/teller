package com.study.teller.sender;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.study.teller.common.MsgUtil;
import com.study.teller.mapper.AccountMapper;
import com.study.teller.vo.AccountVo;

@Component
public class MsgSender {

    @Autowired
    private AccountMapper accountMapper;

    /** 취소된 거래번호 (스텁용) */
    private Set<String> cancelledSet = new HashSet<>();

    /** 거래번호 채번 (스텁용) */
    private int seq = 0;


    public String send(String reqMsg) throws Exception {

        System.out.println(">> 요청전문 : [" + mask(reqMsg) + "]");

        String trCode = MsgUtil.cut(reqMsg, 4, 8).trim();

        String resMsg;
        if ("DEP0001".equals(trCode)) {
            resMsg = makeDepositRes(reqMsg);
        } else if ("WTD0001".equals(trCode)) {
            resMsg = makeWithdrawRes(reqMsg);
        } else if ("DEP0002".equals(trCode)) {
            resMsg = makeCancelRes(reqMsg);
        } else if ("INQ0001".equals(trCode)) {
            resMsg = makeInquiryRes(reqMsg);
        } else if ("INQ0002".equals(trCode)) {
            resMsg = makeHistoryRes();
        } else {
            throw new Exception("알 수 없는 거래코드 : " + trCode);
        }

        System.out.println("<< 응답전문 : [" + resMsg + "]");
        return resMsg;
    }


    /** 입금 */
    private String makeDepositRes(String reqMsg) throws Exception {

        String acctNo = MsgUtil.cut(reqMsg, 39, 14).trim();
        long amount = Long.parseLong(MsgUtil.cut(reqMsg, 73, 15).trim());

        AccountVo acct = accountMapper.selectAccount(acctNo);

        if (acct == null) {
            return fail("DEP0001", "E001", "존재하지 않는 계좌입니다", 0);
        }
        if (!"01".equals(acct.getAcctStatus())) {
            return fail("DEP0001", "E003", "거래할 수 없는 계좌입니다", acct.getBalance());
        }
        if (amount > 1000000) {
            return fail("DEP0001", "E004", "일일 한도를 초과했습니다", acct.getBalance());
        }

        // 잔액 증가
        long newBal = acct.getBalance() + amount;
        acct.setBalance(newBal);
        accountMapper.updateBalance(acct);

        return ok("DEP0001", newBal);
    }


    /** 출금 */
    private String makeWithdrawRes(String reqMsg) throws Exception {

        String acctNo = MsgUtil.cut(reqMsg, 39, 14).trim();
        String passwd = MsgUtil.cut(reqMsg, 53,  4).trim();
        long amount = Long.parseLong(MsgUtil.cut(reqMsg, 57, 15).trim());

        AccountVo acct = accountMapper.selectAccount(acctNo);

        if (acct == null) {
            return fail("WTD0001", "E001", "존재하지 않는 계좌입니다", 0);
        }
        if (!"01".equals(acct.getAcctStatus())) {
            return fail("WTD0001", "E003", "거래할 수 없는 계좌입니다", acct.getBalance());
        }
        if (!acct.getPasswd().equals(passwd)) {
            return fail("WTD0001", "E005", "비밀번호가 일치하지 않습니다", 0);
        }
        if (amount > acct.getBalance()) {
            return fail("WTD0001", "E002", "잔액이 부족합니다", acct.getBalance());
        }

        // 잔액 감소
        long newBal = acct.getBalance() - amount;
        acct.setBalance(newBal);
        accountMapper.updateBalance(acct);

        return ok("WTD0001", newBal);
    }


    /** 입금취소 */
    private String makeCancelRes(String reqMsg) throws Exception {

        String orgTrNo = MsgUtil.cut(reqMsg, 39, 12).trim();

        if (cancelledSet.contains(orgTrNo)) {
            return fail("DEP0002", "E010", "이미 취소된 거래입니다", 0);
        }

        cancelledSet.add(orgTrNo);
        return ok("DEP0002", 0);
    }


    /** 계좌조회 */
    private String makeInquiryRes(String reqMsg) throws Exception {

        String acctNo = MsgUtil.cut(reqMsg, 39, 14).trim();
        AccountVo acct = accountMapper.selectAccount(acctNo);

        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr("INQ0001", 8));

        if (acct == null) {
            sb.append(MsgUtil.padStr("E001", 4));
            sb.append(MsgUtil.padStr("존재하지 않는 계좌입니다", 40));
            sb.append(MsgUtil.padStr("", 14));
            sb.append(MsgUtil.padStr("", 20));
            sb.append(MsgUtil.padStr("", 30));
            sb.append(MsgUtil.padNum("0", 15));
            sb.append(MsgUtil.padStr("", 2));
            return addLength(sb.toString());
        }

        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("정상처리되었습니다", 40));
        sb.append(MsgUtil.padStr(acct.getAcctNo(), 14));
        sb.append(MsgUtil.padStr(acct.getCustNm(), 20));
        sb.append(MsgUtil.padStr(acct.getProdNm(), 30));
        sb.append(MsgUtil.padNum(String.valueOf(acct.getBalance()), 15));
        sb.append(MsgUtil.padStr(acct.getAcctStatus(), 2));
        return addLength(sb.toString());
    }


    /** 거래내역 (스텁 그대로) */
    private String makeHistoryRes() throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr("INQ0002", 8));
        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("정상처리되었습니다", 40));
        sb.append(MsgUtil.padNum("3", 4));
        sb.append(makeItem("20260910", "104800", "입금", "150000", "1150000"));
        sb.append(makeItem("20260909", "142200", "출금", "50000", "1000000"));
        sb.append(makeItem("20260908", "093015", "입금", "200000", "1050000"));
        return addLength(sb.toString());
    }

    private String makeItem(String date, String time, String summary,
                            String amount, String balance) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padNum(date,     8));
        sb.append(MsgUtil.padNum(time,     6));
        sb.append(MsgUtil.padStr(summary, 10));
        sb.append(MsgUtil.padNum(amount,  13));
        sb.append(MsgUtil.padNum(balance, 13));
        return sb.toString();
    }


    /** 정상 응답 (입출금 공통) */
    private String ok(String trCode, long balance) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr(trCode, 8));
        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("정상처리되었습니다", 40));
        sb.append(MsgUtil.padNum(String.valueOf(balance), 15));
        sb.append(MsgUtil.padStr(nextTrNo(), 12));
        return addLength(sb.toString());
    }

    /** 실패 응답 (입출금 공통) */
    private String fail(String trCode, String code, String msg, long balance) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr(trCode, 8));
        sb.append(MsgUtil.padStr(code, 4));
        sb.append(MsgUtil.padStr(msg, 40));
        sb.append(MsgUtil.padNum(String.valueOf(balance), 15));
        sb.append(MsgUtil.padStr("", 12));
        return addLength(sb.toString());
    }


    private String nextTrNo() throws Exception {
        seq++;
        return "TR" + MsgUtil.padNum(String.valueOf(seq), 10);
    }

    private String addLength(String body) throws Exception {
        int totalLen = MsgUtil.byteLength(body) + 4;
        return MsgUtil.padNum(String.valueOf(totalLen), 4) + body;
    }

    /** 로그 마스킹 */
    private String mask(String msg) throws Exception {
        String trCode = MsgUtil.cut(msg, 4, 8).trim();
        if ("WTD0001".equals(trCode)) {
            return MsgUtil.cut(msg, 0, 53) + "****" + msg.substring(57);
        }
        return msg;
    }
}