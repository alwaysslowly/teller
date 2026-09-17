package com.study.teller.vo;

public class TransferReqVo {

    private String trCode;
    private String bankCode;
    private String branchCode;
    private String empNo;
    private String trDate;
    private String trTime;

    private String outAcctNo;    // 출금계좌
    private String passwd;
    private String inAcctNo;     // 입금계좌
    private String amount;

    public String getTrCode() { return trCode; }
    public void setTrCode(String trCode) { this.trCode = trCode; }

    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }

    public String getEmpNo() { return empNo; }
    public void setEmpNo(String empNo) { this.empNo = empNo; }

    public String getTrDate() { return trDate; }
    public void setTrDate(String trDate) { this.trDate = trDate; }

    public String getTrTime() { return trTime; }
    public void setTrTime(String trTime) { this.trTime = trTime; }

    public String getOutAcctNo() { return outAcctNo; }
    public void setOutAcctNo(String outAcctNo) { this.outAcctNo = outAcctNo; }

    public String getPasswd() { return passwd; }
    public void setPasswd(String passwd) { this.passwd = passwd; }

    public String getInAcctNo() { return inAcctNo; }
    public void setInAcctNo(String inAcctNo) { this.inAcctNo = inAcctNo; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }
}