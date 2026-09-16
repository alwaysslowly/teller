package com.study.teller.vo;

public class CashVo {

    private String baseDate;
    private String branchCode;
    private String empNo;
    private long   startAmt;      // 시작 시재
    private long   realAmt;       // 실제 현금
    private String closeYn;

    // 집계 (테이블에 없고 계산해서 채움)
    private long   inAmt;         // 입금 합계
    private long   outAmt;        // 출금 합계
    private long   bookAmt;       // 장부상 현금
    private long   diffAmt;       // 차액

    public String getBaseDate() { return baseDate; }
    public void setBaseDate(String baseDate) { this.baseDate = baseDate; }

    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }

    public String getEmpNo() { return empNo; }
    public void setEmpNo(String empNo) { this.empNo = empNo; }

    public long getStartAmt() { return startAmt; }
    public void setStartAmt(long startAmt) { this.startAmt = startAmt; }

    public long getRealAmt() { return realAmt; }
    public void setRealAmt(long realAmt) { this.realAmt = realAmt; }

    public String getCloseYn() { return closeYn; }
    public void setCloseYn(String closeYn) { this.closeYn = closeYn; }

    public long getInAmt() { return inAmt; }
    public void setInAmt(long inAmt) { this.inAmt = inAmt; }

    public long getOutAmt() { return outAmt; }
    public void setOutAmt(long outAmt) { this.outAmt = outAmt; }

    public long getBookAmt() { return bookAmt; }
    public void setBookAmt(long bookAmt) { this.bookAmt = bookAmt; }

    public long getDiffAmt() { return diffAmt; }
    public void setDiffAmt(long diffAmt) { this.diffAmt = diffAmt; }
}