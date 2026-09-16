package com.study.teller.vo;

public class MenuVo {

    private String menuId;
    private String menuNm;
    private String menuUrl;
    private String authLv;
    private int    sortOrd;
    private String useYn;

    public String getMenuId() { return menuId; }
    public void setMenuId(String menuId) { this.menuId = menuId; }

    public String getMenuNm() { return menuNm; }
    public void setMenuNm(String menuNm) { this.menuNm = menuNm; }

    public String getMenuUrl() { return menuUrl; }
    public void setMenuUrl(String menuUrl) { this.menuUrl = menuUrl; }

    public String getAuthLv() { return authLv; }
    public void setAuthLv(String authLv) { this.authLv = authLv; }

    public int getSortOrd() { return sortOrd; }
    public void setSortOrd(int sortOrd) { this.sortOrd = sortOrd; }

    public String getUseYn() { return useYn; }
    public void setUseYn(String useYn) { this.useYn = useYn; }
}