package com.study.teller.vo;

public class EmpVo {

    private String empNo;
    private String empNm;
    private String bankCode;
    private String branchCode;
    private String passwd;
    private String authLv;
    private String useYn;

    public String getEmpNo() { return empNo; }
    public void setEmpNo(String empNo) { this.empNo = empNo; }

    public String getEmpNm() { return empNm; }
    public void setEmpNm(String empNm) { this.empNm = empNm; }

    public String getBankCode() { return bankCode; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }

    public String getPasswd() { return passwd; }
    public void setPasswd(String passwd) { this.passwd = passwd; }

    public String getAuthLv() { return authLv; }
    public void setAuthLv(String authLv) { this.authLv = authLv; }

    public String getUseYn() { return useYn; }
    public void setUseYn(String useYn) { this.useYn = useYn; }
}