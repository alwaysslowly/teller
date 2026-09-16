package com.study.teller.common;




public class BizException extends RuntimeException {

    private String code;    // 응답코드

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() { return code; }
}