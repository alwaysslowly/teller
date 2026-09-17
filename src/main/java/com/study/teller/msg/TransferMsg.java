package com.study.teller.msg;

import com.study.teller.common.MsgUtil;
import com.study.teller.vo.TransferReqVo;
import com.study.teller.vo.TransferResVo;

public class TransferMsg {

    /** VO → 전문 (요청) */
    public static String pack(TransferReqVo vo) throws Exception {

        StringBuilder sb = new StringBuilder();

        // 공통부
        sb.append(MsgUtil.padStr(vo.getTrCode(),     8));
        sb.append(MsgUtil.padStr(vo.getBankCode(),   3));
        sb.append(MsgUtil.padStr(vo.getBranchCode(), 4));
        sb.append(MsgUtil.padStr(vo.getEmpNo(),      6));
        sb.append(MsgUtil.padNum(vo.getTrDate(),     8));
        sb.append(MsgUtil.padNum(vo.getTrTime(),     6));

        // 개별부
        sb.append(MsgUtil.padStr(vo.getOutAcctNo(), 14));
        sb.append(MsgUtil.padStr(vo.getPasswd(),     4));
        sb.append(MsgUtil.padStr(vo.getInAcctNo(),  14));
        sb.append(MsgUtil.padNum(vo.getAmount(),    15));

        String body = sb.toString();
        int totalLen = MsgUtil.byteLength(body) + 4;
        return MsgUtil.padNum(String.valueOf(totalLen), 4) + body;
    }

    /** 전문 → VO (응답) */
    public static TransferResVo unpack(String msg) throws Exception {

        TransferResVo vo = new TransferResVo();

        vo.setTrCode(  MsgUtil.cut(msg,  4,  8).trim() );
        vo.setResCode( MsgUtil.cut(msg, 12,  4).trim() );
        vo.setResMsg(  MsgUtil.cut(msg, 16, 40).trim() );
        vo.setBalance( MsgUtil.cut(msg, 56, 15).trim() );
        vo.setInCustNm(MsgUtil.cut(msg, 71, 20).trim() );
        vo.setTrNo(    MsgUtil.cut(msg, 91, 12).trim() );

        return vo;
    }
}