package com.study.teller.common;

public class ApiResponse {

    private boolean success;
    private String code;
    private String message;
    private Object data;

    public static ApiResponse ok(Object data) {
        ApiResponse res = new ApiResponse();
        res.success = true;
        res.code = "0000";
        res.message = "정상처리되었습니다";
        res.data = data;
        return res;
    }

    public static ApiResponse fail(String code, String message) {
        ApiResponse res = new ApiResponse();
        res.success = false;
        res.code = code;
        res.message = message;
        return res;
    }

    public boolean isSuccess() { return success; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}