package com.study.teller.msg;

import com.study.teller.common.MsgUtil;
import com.study.teller.vo.DepositReqVo;
import com.study.teller.vo.DepositResVo;

public class DepositMsg {

    /** VO → 전문 (요청) */
    public static String pack(DepositReqVo vo) throws Exception {

        StringBuilder sb = new StringBuilder();

        // 공통부
        sb.append(MsgUtil.padStr(vo.getTrCode(),     8));   // 거래코드
        sb.append(MsgUtil.padStr(vo.getBankCode(),   3));   // 기관코드
        sb.append(MsgUtil.padStr(vo.getBranchCode(), 4));   // 영업점코드
        sb.append(MsgUtil.padStr(vo.getEmpNo(),      6));   // 직원번호
        sb.append(MsgUtil.padNum(vo.getTrDate(),     8));   // 거래일자
        sb.append(MsgUtil.padNum(vo.getTrTime(),     6));   // 거래시간

        // 개별부
        sb.append(MsgUtil.padStr(vo.getAcctNo(),    14));   // 계좌번호
        sb.append(MsgUtil.padStr(vo.getCustNm(),    20));   // 고객명
        sb.append(MsgUtil.padNum(vo.getAmount(),    15));   // 입금금액

        String body = sb.toString();

        // 전문길이는 맨 앞 4자리 = 전체 길이
        int totalLen = MsgUtil.byteLength(body) + 4;
        return MsgUtil.padNum(String.valueOf(totalLen), 4) + body;
    }

    /** 전문 → VO (응답) */
    public static DepositResVo unpack(String msg) throws Exception {

        DepositResVo vo = new DepositResVo();

        vo.setTrCode( MsgUtil.cut(msg,  4,  8).trim() );
        vo.setResCode(MsgUtil.cut(msg, 12,  4).trim() );
        vo.setResMsg( MsgUtil.cut(msg, 16, 40).trim() );
        vo.setBalance(MsgUtil.cut(msg, 56, 15).trim() );
        vo.setTrNo(   MsgUtil.cut(msg, 71, 12).trim() );

        return vo;
    }
}