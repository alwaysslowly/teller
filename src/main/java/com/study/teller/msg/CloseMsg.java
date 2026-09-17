package com.study.teller.msg;

import com.study.teller.common.MsgUtil;
import com.study.teller.vo.CloseReqVo;
import com.study.teller.vo.DepositResVo;

public class CloseMsg {

    /** 해지 요청 */
    public static String packClose(CloseReqVo vo) throws Exception {

        StringBuilder sb = new StringBuilder();
        appendHeader(sb, vo);

        sb.append(MsgUtil.padStr(vo.getAcctNo(), 14));
        sb.append(MsgUtil.padStr(vo.getPasswd(),  4));

        return withLength(sb.toString());
    }

    /** 해지취소 요청 */
    public static String packCancel(CloseReqVo vo) throws Exception {

        StringBuilder sb = new StringBuilder();
        appendHeader(sb, vo);

        sb.append(MsgUtil.padStr(vo.getAcctNo(),  14));
        sb.append(MsgUtil.padStr(vo.getOrgTrNo(), 12));

        return withLength(sb.toString());
    }

    /** 응답 (해지/취소 공통) */
    public static DepositResVo unpack(String msg) throws Exception {

        DepositResVo vo = new DepositResVo();

        vo.setTrCode( MsgUtil.cut(msg,  4,  8).trim() );
        vo.setResCode(MsgUtil.cut(msg, 12,  4).trim() );
        vo.setResMsg( MsgUtil.cut(msg, 16, 40).trim() );
        vo.setBalance(MsgUtil.cut(msg, 56, 15).trim() );
        vo.setTrNo(   MsgUtil.cut(msg, 71, 12).trim() );

        return vo;
    }


    /** 공통부 */
    private static void appendHeader(StringBuilder sb, CloseReqVo vo) throws Exception {
        sb.append(MsgUtil.padStr(vo.getTrCode(),     8));
        sb.append(MsgUtil.padStr(vo.getBankCode(),   3));
        sb.append(MsgUtil.padStr(vo.getBranchCode(), 4));
        sb.append(MsgUtil.padStr(vo.getEmpNo(),      6));
        sb.append(MsgUtil.padNum(vo.getTrDate(),     8));
        sb.append(MsgUtil.padNum(vo.getTrTime(),     6));
    }

    private static String withLength(String body) throws Exception {
        int totalLen = MsgUtil.byteLength(body) + 4;
        return MsgUtil.padNum(String.valueOf(totalLen), 4) + body;
    }
}