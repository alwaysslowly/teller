package com.study.teller.vo;

public class InquiryReqVo {

    // 공통부
    private String trCode;
    private String bankCode;
    private String branchCode;
    private String empNo;
    private String trDate;
    private String trTime;

    // 개별부
    private String acctNo;

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

    public String getAcctNo() { return acctNo; }
    public void setAcctNo(String acctNo) { this.acctNo = acctNo; }
}