package com.study.teller.vo;

public class NewAcctReqVo {

    // 공통부
    private String trCode;
    private String bankCode;
    private String branchCode;
    private String empNo;
    private String trDate;
    private String trTime;

    // 개별부
    private String custNm;
    private String prodCode;
    private String passwd;
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

    public String getCustNm() { return custNm; }
    public void setCustNm(String custNm) { this.custNm = custNm; }

    public String getProdCode() { return prodCode; }
    public void setProdCode(String prodCode) { this.prodCode = prodCode; }

    public String getPasswd() { return passwd; }
    public void setPasswd(String passwd) { this.passwd = passwd; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }
}