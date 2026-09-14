package com.study.teller.msg;

import com.study.teller.common.MsgUtil;
import com.study.teller.vo.InquiryReqVo;
import com.study.teller.vo.InquiryResVo;

public class InquiryMsg {

    /** VO → 전문 (요청) */
    public static String pack(InquiryReqVo vo) throws Exception {

        StringBuilder sb = new StringBuilder();

        // 공통부
        sb.append(MsgUtil.padStr(vo.getTrCode(),     8));
        sb.append(MsgUtil.padStr(vo.getBankCode(),   3));
        sb.append(MsgUtil.padStr(vo.getBranchCode(), 4));
        sb.append(MsgUtil.padStr(vo.getEmpNo(),      6));
        sb.append(MsgUtil.padNum(vo.getTrDate(),     8));
        sb.append(MsgUtil.padNum(vo.getTrTime(),     6));

        // 개별부
        sb.append(MsgUtil.padStr(vo.getAcctNo(),    14));

        String body = sb.toString();
        int totalLen = MsgUtil.byteLength(body) + 4;
        return MsgUtil.padNum(String.valueOf(totalLen), 4) + body;
    }

    /** 전문 → VO (응답) */
    public static InquiryResVo unpack(String msg) throws Exception {

        InquiryResVo vo = new InquiryResVo();

        vo.setTrCode(    MsgUtil.cut(msg,  4,  8).trim() );
        vo.setResCode(   MsgUtil.cut(msg, 12,  4).trim() );
        vo.setResMsg(    MsgUtil.cut(msg, 16, 40).trim() );
        vo.setAcctNo(    MsgUtil.cut(msg, 56, 14).trim() );
        vo.setCustNm(    MsgUtil.cut(msg, 70, 20).trim() );
        vo.setProdNm(    MsgUtil.cut(msg, 90, 30).trim() );
        vo.setBalance(   MsgUtil.cut(msg,120, 15).trim() );
        vo.setAcctStatus(MsgUtil.cut(msg,135,  2).trim() );

        return vo;
    }
}