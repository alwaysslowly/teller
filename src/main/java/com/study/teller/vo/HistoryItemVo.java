package com.study.teller.vo;

public class HistoryItemVo {

    private String trDate;      // 거래일자
    private String trTime;      // 거래시간
    private String summary;     // 적요
    private String amount;      // 거래금액
    private String balance;     // 거래후잔액

    public String getTrDate() { return trDate; }
    public void setTrDate(String trDate) { this.trDate = trDate; }

    public String getTrTime() { return trTime; }
    public void setTrTime(String trTime) { this.trTime = trTime; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getBalance() { return balance; }
    public void setBalance(String balance) { this.balance = balance; }
}