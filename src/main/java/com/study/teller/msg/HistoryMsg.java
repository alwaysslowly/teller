package com.study.teller.msg;

import com.study.teller.common.MsgUtil;
import com.study.teller.vo.HistoryItemVo;
import com.study.teller.vo.HistoryReqVo;
import com.study.teller.vo.HistoryResVo;

public class HistoryMsg {

    /** 반복부 1건의 길이 */
    private static final int ITEM_LEN = 50;
    
    /** VO → 전문 (요청) */
    public static String pack(HistoryReqVo vo) throws Exception {

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
        sb.append(MsgUtil.padNum(vo.getFromDate(),   8));
        sb.append(MsgUtil.padNum(vo.getToDate(),     8));

        String body = sb.toString();
        int totalLen = MsgUtil.byteLength(body) + 4;
        return MsgUtil.padNum(String.valueOf(totalLen), 4) + body;
    }

    /** 전문 → VO (응답) */
    public static HistoryResVo unpack(String msg) throws Exception {

        HistoryResVo vo = new HistoryResVo();

        // 헤더
        vo.setTrCode( MsgUtil.cut(msg,  4,  8).trim() );
        vo.setResCode(MsgUtil.cut(msg, 12,  4).trim() );
        vo.setResMsg( MsgUtil.cut(msg, 16, 40).trim() );

        // 건수
        String cntStr = MsgUtil.cut(msg, 56, 4).trim();
        int cnt = Integer.parseInt(cntStr);
        vo.setTotalCount(cnt);

        // ★ 반복부 - 건수만큼 반복
        for (int i = 0; i < cnt; i++) {

            int offset = 60 + (i * ITEM_LEN);   // 시작 위치 계산

            HistoryItemVo item = new HistoryItemVo();
            item.setTrDate( MsgUtil.cut(msg, offset,      8).trim() );
            item.setTrTime( MsgUtil.cut(msg, offset +  8, 6).trim() );
            item.setSummary(MsgUtil.cut(msg, offset + 14, 10).trim() );
            item.setAmount( MsgUtil.cut(msg, offset + 24, 13).trim() );
            item.setBalance(MsgUtil.cut(msg, offset + 37, 13).trim() );

            vo.getList().add(item);    // 목록에 추가
        }

        return vo;
    }
}