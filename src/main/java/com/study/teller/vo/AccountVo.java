package com.study.teller.vo;

public class AccountVo {

    private String acctNo;
    private String bankCode;
    private String custNm;
    private String prodNm;
    private String passwd;
    private long   balance;
    private String acctStatus;

    public String getAcctNo() { return acctNo; }
    public void setAcctNo(String acctNo) { this.acctNo = acctNo; }

    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }

    public String getCustNm() { return custNm; }
    public void setCustNm(String custNm) { this.custNm = custNm; }

    public String getProdNm() { return prodNm; }
    public void setProdNm(String prodNm) { this.prodNm = prodNm; }

    public String getPasswd() { return passwd; }
    public void setPasswd(String passwd) { this.passwd = passwd; }

    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }

    public String getAcctStatus() { return acctStatus; }
    public void setAcctStatus(String acctStatus) { this.acctStatus = acctStatus; }
}