package com.study.teller.common;

public class MsgUtil {

    public static final String CHARSET = "EUC-KR";

    /** EUC-KR 바이트 길이 */
    public static int byteLength(String src) throws Exception {
        if (src == null) return 0;
        return src.getBytes(CHARSET).length;
    }

    /** 숫자 필드: 좌측 0 패딩 */
    public static String padNum(String src, int len) throws Exception {
        if (src == null) src = "";
        src = src.trim();

        int cur = byteLength(src);
        if (cur > len) {
            return cut(src, cur - len, len);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len - cur; i++) sb.append('0');
        sb.append(src);
        return sb.toString();
    }

    /** 문자 필드: 우측 공백 패딩 */
    public static String padStr(String src, int len) throws Exception {
        if (src == null) src = "";

        int cur = byteLength(src);
        if (cur > len) {
            src = cut(src, 0, len);
            cur = byteLength(src);
        }

        StringBuilder sb = new StringBuilder(src);
        for (int i = 0; i < len - cur; i++) sb.append(' ');
        return sb.toString();
    }

    /** 바이트 기준 자르기 */
    public static String cut(String src, int offset, int len) throws Exception {
        if (src == null) return "";

        byte[] b = src.getBytes(CHARSET);
        if (offset >= b.length) return "";
        if (offset + len > b.length) len = b.length - offset;

        String result = new String(b, offset, len, CHARSET);

        while (byteLength(result) > len) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}