package com.study.teller.vo;

public class TransferResVo {

    private String trCode;
    private String resCode;
    private String resMsg;
    private String balance;      // 출금후잔액
    private String inCustNm;     // 입금계좌 고객명
    private String trNo;

    public String getTrCode() { return trCode; }
    public void setTrCode(String trCode) { this.trCode = trCode; }

    public String getResCode() { return resCode; }
    public void setResCode(String resCode) { this.resCode = resCode; }

    public String getResMsg() { return resMsg; }
    public void setResMsg(String resMsg) { this.resMsg = resMsg; }

    public String getBalance() { return balance; }
    public void setBalance(String balance) { this.balance = balance; }

    public String getInCustNm() { return inCustNm; }
    public void setInCustNm(String inCustNm) { this.inCustNm = inCustNm; }

    public String getTrNo() { return trNo; }
    public void setTrNo(String trNo) { this.trNo = trNo; }
}