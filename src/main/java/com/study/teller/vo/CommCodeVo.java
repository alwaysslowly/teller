package com.study.teller.vo;

public class CommCodeVo {

    private String grpCode;
    private String code;
    private String codeNm;
    private int    sortOrd;
    private String useYn;

    public String getGrpCode() { return grpCode; }
    public void setGrpCode(String grpCode) { this.grpCode = grpCode; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getCodeNm() { return codeNm; }
    public void setCodeNm(String codeNm) { this.codeNm = codeNm; }

    public int getSortOrd() { return sortOrd; }
    public void setSortOrd(int sortOrd) { this.sortOrd = sortOrd; }

    public String getUseYn() { return useYn; }
    public void setUseYn(String useYn) { this.useYn = useYn; }
}