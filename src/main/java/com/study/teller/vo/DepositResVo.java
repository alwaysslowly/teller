package com.study.teller.vo;

public class DepositResVo {

    private String trCode;      // 거래코드
    private String resCode;     // 응답코드
    private String resMsg;      // 응답메시지
    private String balance;     // 거래후잔액
    private String trNo;        // 거래번호

    public String getTrCode() { return trCode; }
    public void setTrCode(String trCode) { this.trCode = trCode; }

    public String getResCode() { return resCode; }
    public void setResCode(String resCode) { this.resCode = resCode; }

    public String getResMsg() { return resMsg; }
    public void setResMsg(String resMsg) { this.resMsg = resMsg; }

    public String getBalance() { return balance; }
    public void setBalance(String balance) { this.balance = balance; }

    public String getTrNo() { return trNo; }
    public void setTrNo(String trNo) { this.trNo = trNo; }
}