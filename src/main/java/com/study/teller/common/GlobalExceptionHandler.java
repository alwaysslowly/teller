package com.study.teller.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 업무 예외 - 잔액부족, 한도초과 등 */
    @ExceptionHandler(BizException.class)
    public ApiResponse handleBiz(BizException e) {
        System.out.println("[업무실패] " + e.getCode() + " : " + e.getMessage());
        return ApiResponse.fail(e.getCode(), e.getMessage());
    }

    /** 시스템 예외 - 버그, 장애 */
    @ExceptionHandler(Exception.class)
    public ApiResponse handleAll(Exception e) {
        e.printStackTrace();
        return ApiResponse.fail("9999", "시스템 오류가 발생했습니다.");
    }
}