package com.study.teller.sender;

import com.study.teller.common.MsgUtil;

public class MsgSender {

    public static String send(String reqMsg) throws Exception {

        System.out.println(">> 요청전문 : [" + reqMsg + "]");

        // 전문에서 거래코드를 꺼낸다 (4번째부터 8자리)
        String trCode = MsgUtil.cut(reqMsg, 4, 8).trim();

        String resMsg;
        if ("DEP0001".equals(trCode)) {
            resMsg = makeDepositRes(reqMsg);
        } else if ("INQ0001".equals(trCode)) {
            resMsg = makeInquiryRes();
        } else if ("DEP0002".equals(trCode)) {     // ← 추가
            resMsg = makeCancelRes();              // ← 추가
        } else if ("DEP0002".equals(trCode)) {
            resMsg = makeCancelRes();
        } else if ("INQ0002".equals(trCode)) {     // ← 추가
            resMsg = makeHistoryRes();             // ← 추가
        } 
        	else {
            throw new Exception("알 수 없는 거래코드 : " + trCode);
        }

        System.out.println("<< 응답전문 : [" + resMsg + "]");
        return resMsg;
    }

    /** 입금 응답 */
    private static String makeDepositRes(String reqMsg) throws Exception {

        String amtStr = MsgUtil.cut(reqMsg, 73, 15).trim();
        long amount = Long.parseLong(amtStr);

        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr("DEP0001", 8));

        if (amount > 1000000) {
            sb.append(MsgUtil.padStr("E004", 4));
            sb.append(MsgUtil.padStr("일일 한도를 초과했습니다", 40));
            sb.append(MsgUtil.padNum("0", 15));
            sb.append(MsgUtil.padStr("", 12));
            return addLength(sb.toString());
        }

        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("정상처리되었습니다", 40));
        sb.append(MsgUtil.padNum(String.valueOf(1000000 + amount), 15));
        sb.append(MsgUtil.padStr(nextTrNo(), 12));        // ← 여기
        return addLength(sb.toString());
    }

    /** 거래번호 채번 (스텁용) */
    private static int seq = 0;
    private static String nextTrNo() throws Exception {
        seq++;
        return "TR" + MsgUtil.padNum(String.valueOf(seq), 10);
    }
    
    
    /** 조회 응답 */
    private static String makeInquiryRes() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr("INQ0001", 8));
        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("정상처리되었습니다", 40));
        sb.append(MsgUtil.padStr("1234567890", 14));
        sb.append(MsgUtil.padStr("홍길동", 20));
        sb.append(MsgUtil.padStr("정기예금 12개월", 30));
        sb.append(MsgUtil.padNum("1150000", 15));
        sb.append(MsgUtil.padStr("01", 2));
        return addLength(sb.toString());
    }
    
    /** 입금취소 응답 */
    private static String makeCancelRes() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padStr("DEP0002", 8));
        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("취소처리되었습니다", 40));
        sb.append(MsgUtil.padNum("1000000", 15));        // 취소 후 잔액
        sb.append(MsgUtil.padStr("TR2026091002", 12));   // 취소거래의 거래번호
        return addLength(sb.toString());
    }
    
    /** 거래내역조회 응답 */
    private static String makeHistoryRes() throws Exception {

        StringBuilder sb = new StringBuilder();

        // 헤더
        sb.append(MsgUtil.padStr("INQ0002", 8));
        sb.append(MsgUtil.padStr("0000", 4));
        sb.append(MsgUtil.padStr("정상처리되었습니다", 40));

        // 건수
        sb.append(MsgUtil.padNum("3", 4));

        // 반복부 1건
        sb.append(makeItem("20260910", "104800", "입금", "150000", "1150000"));
        sb.append(makeItem("20260909", "142200", "출금", "50000", "1000000"));
        sb.append(makeItem("20260908", "093015", "입금", "200000", "1050000"));

        return addLength(sb.toString());
    }

    /** 반복부 1건 만들기 (50바이트) */
    private static String makeItem(String date, String time, String summary,
                                   String amount, String balance) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(MsgUtil.padNum(date,     8));
        sb.append(MsgUtil.padNum(time,     6));
        sb.append(MsgUtil.padStr(summary, 10));
        sb.append(MsgUtil.padNum(amount,  13));
        sb.append(MsgUtil.padNum(balance, 13));
        return sb.toString();
    }
    
    /** 앞에 전문길이 붙이기 */
    private static String addLength(String body) throws Exception {
        int totalLen = MsgUtil.byteLength(body) + 4;
        return MsgUtil.padNum(String.valueOf(totalLen), 4) + body;
    }
}