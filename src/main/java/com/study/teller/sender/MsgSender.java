package com.study.teller.sender;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.study.teller.common.MsgUtil;
import com.study.teller.mapper.AccountMapper;
import com.study.teller.mapper.TrHistMapper;
import com.study.teller.vo.AccountVo;
import com.study.teller.vo.TrHistVo;

@Component
public class MsgSender {

    @Autowired
    private AccountMapper accountMapper;
    
    @Autowired
    private TrHistMapper trHistMapper;

    /** 취소된 거래번호 (스텁용) */
    private Set<String> cancelledSet = new HashSet<>();

    /** 거래번호 채번 (스텁용) */
    private int seq = 0;

    private Map<String, Long> closedMap = new HashMap<>();
    
    @Transactional
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
            resMsg = makeHistoryRes(reqMsg);
        } else if ("NEW0001".equals(trCode)) {
            resMsg = makeNewAcctRes(reqMsg);
        } else if ("TRF0001".equals(trCode)) {     // ← 추가
            resMsg = makeTransferRes(reqMsg);      // ← 추가
        } else if ("CLS0001".equals(trCode)) {     // ← 추가
            resMsg = makeCloseRes(reqMsg);
        } else if ("CLS0002".equals(trCode)) {     // ← 추가
            resMsg = makeCloseCancelRes(reqMsg);
        }  else {
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
    
    /** 계좌개설 */
    private String makeNewAcctRes(String reqMsg) throws Exception {

        String bankCode = MsgUtil.cut(reqMsg, 12,  3).trim();
        String custNm   = MsgUtil.cut(reqMsg, 39, 20).trim();
        String prodCode = MsgUtil.cut(reqMsg, 59,  4).trim();
        String passwd   = MsgUtil.cut(reqMsg, 63,  4).trim();
        long   amount   = Long.parseLong(MsgUtil.cut(reqMsg, 67, 15).trim());

        // 상품코드 → 상품명
        String prodNm = toProdNm(prodCode);
        if (prodNm == null) {
            return newAcctFail("E006", "취급하지 않는 상품입니다");
        }

        // 계좌번호 채번
        String acctNo = nextAcctNo(bankCode);

        // 계좌 생성
        AccountVo acct = new AccountVo();
        acct.setAcctNo(acctNo);
        acct.setBankCode(bankCode);
        acct.setCustNm(custNm);
        acct.setProdNm(prodNm);
        acct.setPasswd(passwd);
        acct.setBalance(amount);

        accountMapper.insertAccount(acct);

        // 정상 응답
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr("NEW0001", 8));
        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("정상처리되었습니다", 40));
        sb.append(MsgUtil.padStr(acctNo, 14));
        sb.append(MsgUtil.padNum(String.valueOf(amount), 15));
        sb.append(MsgUtil.padStr(nextTrNo(), 12));
        return addLength(sb.toString());
    }


    /** 계좌개설 실패 응답 */
    private String newAcctFail(String code, String msg) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr("NEW0001", 8));
        sb.append(MsgUtil.padStr(code, 4));
        sb.append(MsgUtil.padStr(msg, 40));
        sb.append(MsgUtil.padStr("", 14));
        sb.append(MsgUtil.padNum("0", 15));
        sb.append(MsgUtil.padStr("", 12));
        return addLength(sb.toString());
    }


    /** 상품코드 → 상품명 */
    private String toProdNm(String prodCode) {
        if ("P001".equals(prodCode)) return "보통예금";
        if ("P002".equals(prodCode)) return "정기예금 12개월";
        if ("P003".equals(prodCode)) return "자유적금 24개월";
        return null;
    }


    /** 계좌번호 채번 */
    private String nextAcctNo(String bankCode) throws Exception {

        String max = accountMapper.selectMaxAcctNo(bankCode);

        long next;
        if (max == null || max.trim().isEmpty()) {
            next = 1;
        } else {
            next = Long.parseLong(max.trim()) + 1;
        }

        return MsgUtil.padNum(String.valueOf(next), 10);
    }
    
    /** 거래내역 조회 */
    private String makeHistoryRes(String reqMsg) throws Exception {

        String acctNo   = MsgUtil.cut(reqMsg, 39, 14).trim();
        String fromDate = MsgUtil.cut(reqMsg, 53,  8).trim();
        String toDate   = MsgUtil.cut(reqMsg, 61,  8).trim();

        TrHistVo param = new TrHistVo();
        param.setAcctNo(acctNo);
        param.setFromDate(fromDate);
        param.setToDate(toDate);

        List<TrHistVo> list = trHistMapper.selectHistByPeriod(param);

        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr("INQ0002", 8));
        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("정상처리되었습니다", 40));
        sb.append(MsgUtil.padNum(String.valueOf(list.size()), 4));

        for (TrHistVo h : list) {
            sb.append(makeItem(
                h.getTrDate(),
                h.getTrTime(),
                toSummary(h.getTrCode(), h.getAmount()),
                String.valueOf(Math.abs(h.getAmount())),
                String.valueOf(h.getBalance())
            ));
        }

        return addLength(sb.toString());
    }

    /** 거래코드 → 적요 */
    private String toSummary(String trCode, long amount) {
        if ("DEP0001".equals(trCode)) return "입금";
        if ("WTD0001".equals(trCode)) return "출금";
        if ("DEP0002".equals(trCode)) return "입금취소";
        if ("NEW0001".equals(trCode)) return "신규";
        return trCode;
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
    
    /** 이체 */
    private String makeTransferRes(String reqMsg) throws Exception {

        String outAcctNo = MsgUtil.cut(reqMsg, 39, 14).trim();
        String passwd    = MsgUtil.cut(reqMsg, 53,  4).trim();
        String inAcctNo  = MsgUtil.cut(reqMsg, 57, 14).trim();
        long   amount    = Long.parseLong(MsgUtil.cut(reqMsg, 71, 15).trim());

        // 같은 계좌로 이체 불가
        if (outAcctNo.equals(inAcctNo)) {
            return transferFail("E007", "출금계좌와 입금계좌가 같습니다", 0, "");
        }

        // 출금계좌 확인
        AccountVo out = accountMapper.selectAccount(outAcctNo);
        if (out == null) {
            return transferFail("E001", "출금계좌가 존재하지 않습니다", 0, "");
        }
        if (!"01".equals(out.getAcctStatus())) {
            return transferFail("E003", "출금계좌가 거래할 수 없는 상태입니다", out.getBalance(), "");
        }
        if (!out.getPasswd().equals(passwd)) {
            return transferFail("E005", "비밀번호가 일치하지 않습니다", 0, "");
        }
        // 입금계좌 확인
        AccountVo in = accountMapper.selectAccount(inAcctNo);
        if (in == null) {
            return transferFail("E008", "입금계좌가 존재하지 않습니다", out.getBalance(), "");
        }
        if (!"01".equals(in.getAcctStatus())) {
            return transferFail("E009", "입금계좌가 거래할 수 없는 상태입니다", out.getBalance(), "");
        }
        // 잔액 확인
        if (amount > out.getBalance()) {
            return transferFail("E002", "잔액이 부족합니다", out.getBalance(), "");
        }
        // ★ 출금 + 입금
        long outBal = out.getBalance() - amount;
        out.setBalance(outBal);
        accountMapper.updateBalance(out);

        in.setBalance(in.getBalance() + amount);
        accountMapper.updateBalance(in);

        // 정상 응답
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr("TRF0001", 8));
        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("정상처리되었습니다", 40));
        sb.append(MsgUtil.padNum(String.valueOf(outBal), 15));
        sb.append(MsgUtil.padStr(in.getCustNm(), 20));
        sb.append(MsgUtil.padStr(nextTrNo(), 12));
        return addLength(sb.toString());
    }


    /** 이체 실패 응답 */
    private String transferFail(String code, String msg, long balance, String custNm) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr("TRF0001", 8));
        sb.append(MsgUtil.padStr(code, 4));
        sb.append(MsgUtil.padStr(msg, 40));
        sb.append(MsgUtil.padNum(String.valueOf(balance), 15));
        sb.append(MsgUtil.padStr(custNm, 20));
        sb.append(MsgUtil.padStr("", 12));
        return addLength(sb.toString());
    }
    
    /** 계좌해지 */
    private String makeCloseRes(String reqMsg) throws Exception {

        String acctNo = MsgUtil.cut(reqMsg, 39, 14).trim();
        String passwd = MsgUtil.cut(reqMsg, 53,  4).trim();

        AccountVo acct = accountMapper.selectAccount(acctNo);

        if (acct == null) {
            return closeFail("CLS0001", "E001", "존재하지 않는 계좌입니다", 0);
        }
        if ("02".equals(acct.getAcctStatus())) {
            return closeFail("CLS0001", "E011", "이미 해지된 계좌입니다", 0);
        }
        if (!"01".equals(acct.getAcctStatus())) {
            return closeFail("CLS0001", "E003", "거래할 수 없는 계좌입니다", acct.getBalance());
        }
        if (!acct.getPasswd().equals(passwd)) {
            return closeFail("CLS0001", "E005", "비밀번호가 일치하지 않습니다", 0);
        }

        long payAmt = acct.getBalance();
        String trNo = nextTrNo();

        // 해지 : 잔액 0, 상태 02
        acct.setBalance(0);
        acct.setAcctStatus("02");
        accountMapper.updateClose(acct);

        // 취소를 위해 기억
        closedMap.put(trNo, payAmt);

        return closeOk("CLS0001", payAmt, trNo);
    }


    /** 해지취소 */
    private String makeCloseCancelRes(String reqMsg) throws Exception {

        String acctNo  = MsgUtil.cut(reqMsg, 39, 14).trim();
        String orgTrNo = MsgUtil.cut(reqMsg, 53, 12).trim();

        AccountVo acct = accountMapper.selectAccount(acctNo);

        if (acct == null) {
            return closeFail("CLS0002", "E001", "존재하지 않는 계좌입니다", 0);
        }
        if (!"02".equals(acct.getAcctStatus())) {
            return closeFail("CLS0002", "E012", "해지된 계좌가 아닙니다", 0);
        }

        Long orgBal = closedMap.get(orgTrNo);
        if (orgBal == null) {
            return closeFail("CLS0002", "E013", "해지 거래를 찾을 수 없습니다", 0);
        }

        // 복구 : 잔액 되돌리고 상태 01
        acct.setBalance(orgBal);
        acct.setAcctStatus("01");
        accountMapper.updateClose(acct);

        closedMap.remove(orgTrNo);

        return closeOk("CLS0002", orgBal, nextTrNo());
    }


    /** 해지 정상 응답 */
    private String closeOk(String trCode, long amount, String trNo) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr(trCode, 8));
        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("정상처리되었습니다", 40));
        sb.append(MsgUtil.padNum(String.valueOf(amount), 15));
        sb.append(MsgUtil.padStr(trNo, 12));
        return addLength(sb.toString());
    }

    /** 해지 실패 응답 */
    private String closeFail(String trCode, String code, String msg, long amount) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr(trCode, 8));
        sb.append(MsgUtil.padStr(code, 4));
        sb.append(MsgUtil.padStr(msg, 40));
        sb.append(MsgUtil.padNum(String.valueOf(amount), 15));
        sb.append(MsgUtil.padStr("", 12));
        return addLength(sb.toString());
    }
    
    
    
}