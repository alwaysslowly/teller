package com.study.teller.vo;

import java.util.ArrayList;
import java.util.List;

public class HistoryResVo {

    private String trCode;
    private String resCode;
    private String resMsg;
    private int totalCount;                 // 건수

    private List<HistoryItemVo> list = new ArrayList<>();   // ★ 목록

    public String getTrCode() { return trCode; }
    public void setTrCode(String trCode) { this.trCode = trCode; }

    public String getResCode() { return resCode; }
    public void setResCode(String resCode) { this.resCode = resCode; }

    public String getResMsg() { return resMsg; }
    public void setResMsg(String resMsg) { this.resMsg = resMsg; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public List<HistoryItemVo> getList() { return list; }
    public void setList(List<HistoryItemVo> list) { this.list = list; }
}