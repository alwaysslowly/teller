package com.study.teller.vo;

public class CloseReqVo {

    private String trCode;
    private String bankCode;
    private String branchCode;
    private String empNo;
    private String trDate;
    private String trTime;

    private String acctNo;
    private String passwd;
    private String orgTrNo;    // 취소 시 사용

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

    public String getPasswd() { return passwd; }
    public void setPasswd(String passwd) { this.passwd = passwd; }

    public String getOrgTrNo() { return orgTrNo; }
    public void setOrgTrNo(String orgTrNo) { this.orgTrNo = orgTrNo; }
}