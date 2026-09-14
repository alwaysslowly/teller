package com.study.teller.vo;

public class InquiryResVo {

    private String trCode;
    private String resCode;
    private String resMsg;

    private String acctNo;      // 계좌번호
    private String custNm;      // 고객명
    private String prodNm;      // 상품명
    private String balance;     // 잔액
    private String acctStatus;  // 계좌상태

    public String getTrCode() { return trCode; }
    public void setTrCode(String trCode) { this.trCode = trCode; }

    public String getResCode() { return resCode; }
    public void setResCode(String resCode) { this.resCode = resCode; }

    public String getResMsg() { return resMsg; }
    public void setResMsg(String resMsg) { this.resMsg = resMsg; }

    public String getAcctNo() { return acctNo; }
    public void setAcctNo(String acctNo) { this.acctNo = acctNo; }

    public String getCustNm() { return custNm; }
    public void setCustNm(String custNm) { this.custNm = custNm; }

    public String getProdNm() { return prodNm; }
    public void setProdNm(String prodNm) { this.prodNm = prodNm; }

    public String getBalance() { return balance; }
    public void setBalance(String balance) { this.balance = balance; }

    public String getAcctStatus() { return acctStatus; }
    public void setAcctStatus(String acctStatus) { this.acctStatus = acctStatus; }
}