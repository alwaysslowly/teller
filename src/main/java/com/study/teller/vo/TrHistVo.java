package com.study.teller.vo;

public class TrHistVo {

    private String trNo;
    private String bankCode;
    private String branchCode;
    private String empNo;
    private String trCode;
    private String trDate;
    private String trTime;
    private String acctNo;
    private String custNm;
    private long   amount;
    private long   balance;
    private String resCode;
    private String resMsg;
    private String fromDate;
    private String toDate;

    public String getTrNo() { return trNo; }
    public void setTrNo(String trNo) { this.trNo = trNo; }

    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }

    public String getEmpNo() { return empNo; }
    public void setEmpNo(String empNo) { this.empNo = empNo; }

    public String getTrCode() { return trCode; }
    public void setTrCode(String trCode) { this.trCode = trCode; }

    public String getTrDate() { return trDate; }
    public void setTrDate(String trDate) { this.trDate = trDate; }

    public String getTrTime() { return trTime; }
    public void setTrTime(String trTime) { this.trTime = trTime; }

    public String getAcctNo() { return acctNo; }
    public void setAcctNo(String acctNo) { this.acctNo = acctNo; }

    public String getCustNm() { return custNm; }
    public void setCustNm(String custNm) { this.custNm = custNm; }

    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }

    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }

    public String getResCode() { return resCode; }
    public void setResCode(String resCode) { this.resCode = resCode; }

    public String getResMsg() { return resMsg; }
    public void setResMsg(String resMsg) { this.resMsg = resMsg; }
    
    public String getFromDate() { return fromDate; }
    public void setFromDate(String fromDate) { this.fromDate = fromDate; }

    public String getToDate() { return toDate; }
    public void setToDate(String toDate) { this.toDate = toDate; }
    
    
}