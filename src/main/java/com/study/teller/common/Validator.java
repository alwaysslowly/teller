 package com.study.teller.common;

public class Validator {

    /** 이름 뒤에 알맞은 조사를 붙인다 */
    private static String josa(String name) {
        char last = name.charAt(name.length() - 1);
        boolean hasJong = (last - 0xAC00) % 28 != 0;
        return name + (hasJong ? "은" : "는");
    }

    /** 필수값 확인 */
    public static void required(String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            throw new BizException("V001", josa(name) + " 필수입니다.");
        }
    }

    /** 숫자만 들어있는지 */
    public static void numeric(String value, String name) {
        if (value == null || !value.matches("^[0-9]+$")) {
            throw new BizException("V002", josa(name) + " 숫자만 입력할 수 있습니다.");
        }
    }

    /** 금액이 0보다 큰지 */
    public static void positive(String value, String name) {
        numeric(value, name);
        if (Long.parseLong(value) <= 0) {
            throw new BizException("V003", josa(name) + " 0보다 커야 합니다.");
        }
    }

    /** 최대 길이 확인 */
    public static void maxLength(String value, int max, String name) {
        if (value != null && value.length() > max) {
            throw new BizException("V004", josa(name) + " " + max + "자를 넘을 수 없습니다.");
        }
    }

    /** 날짜 형식(yyyyMMdd) 확인 */
    public static void date(String value, String name) {
        if (value == null || !value.matches("^\\d{8}$")) {
            throw new BizException("V005", josa(name) + " yyyyMMdd 형식이어야 합니다.");
        }
    }
}