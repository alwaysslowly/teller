package com.study.teller.vo;

public class DepositReqVo {

    // 공통부
    private String trCode;      // 거래코드
    private String bankCode;    // 기관코드
    private String branchCode;  // 영업점코드
    private String empNo;       // 직원번호
    private String trDate;      // 거래일자
    private String trTime;      // 거래시간

    // 개별부
    private String acctNo;      // 계좌번호
    private String custNm;      // 고객명
    private String amount;      // 입금금액

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

    public String getCustNm() { return custNm; }
    public void setCustNm(String custNm) { this.custNm = custNm; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }
}