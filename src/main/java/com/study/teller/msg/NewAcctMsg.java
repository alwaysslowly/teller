package com.study.teller.msg;

import com.study.teller.common.MsgUtil;
import com.study.teller.vo.NewAcctReqVo;
import com.study.teller.vo.NewAcctResVo;

public class NewAcctMsg {

    /** VO → 전문 (요청) */
    public static String pack(NewAcctReqVo vo) throws Exception {

        StringBuilder sb = new StringBuilder();

        // 공통부
        sb.append(MsgUtil.padStr(vo.getTrCode(),     8));
        sb.append(MsgUtil.padStr(vo.getBankCode(),   3));
        sb.append(MsgUtil.padStr(vo.getBranchCode(), 4));
        sb.append(MsgUtil.padStr(vo.getEmpNo(),      6));
        sb.append(MsgUtil.padNum(vo.getTrDate(),     8));
        sb.append(MsgUtil.padNum(vo.getTrTime(),     6));

        // 개별부
        sb.append(MsgUtil.padStr(vo.getCustNm(),    20));
        sb.append(MsgUtil.padStr(vo.getProdCode(),   4));
        sb.append(MsgUtil.padStr(vo.getPasswd(),     4));
        sb.append(MsgUtil.padNum(vo.getAmount(),    15));

        String body = sb.toString();
        int totalLen = MsgUtil.byteLength(body) + 4;
        return MsgUtil.padNum(String.valueOf(totalLen), 4) + body;
    }

    /** 전문 → VO (응답) */
    public static NewAcctResVo unpack(String msg) throws Exception {

        NewAcctResVo vo = new NewAcctResVo();

        vo.setTrCode( MsgUtil.cut(msg,  4,  8).trim() );
        vo.setResCode(MsgUtil.cut(msg, 12,  4).trim() );
        vo.setResMsg( MsgUtil.cut(msg, 16, 40).trim() );
        vo.setAcctNo( MsgUtil.cut(msg, 56, 14).trim() );
        vo.setBalance(MsgUtil.cut(msg, 70, 15).trim() );
        vo.setTrNo(   MsgUtil.cut(msg, 85, 12).trim() );

        return vo;
    }
}