package com.study.teller.vo;

public class CancelReqVo {
	

    // 공통부
    private String trCode;      // 거래코드
    private String bankCode;    // 기관코드
    private String branchCode;  // 영업점코드
    private String empNo;       // 직원번호
    private String trDate;      // 거래일자
    private String trTime;      // 거래시간

    // 개별부
    private String orgTrNo;     // 원거래번호
    private String cancelRsn;   // 취소사유
    
	public String getTrCode() {
		return trCode;
	}
	public void setTrCode(String trCode) {
		this.trCode = trCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getTrDate() {
		return trDate;
	}
	public void setTrDate(String trDate) {
		this.trDate = trDate;
	}
	public String getTrTime() {
		return trTime;
	}
	public void setTrTime(String trTime) {
		this.trTime = trTime;
	}
	public String getOrgTrNo() {
		return orgTrNo;
	}
	public void setOrgTrNo(String orgTrNo) {
		this.orgTrNo = orgTrNo;
	}
	public String getCancelRsn() {
		return cancelRsn;
	}
	public void setCancelRsn(String cancelRsn) {
		this.cancelRsn = cancelRsn;
	}
    	
    
}
