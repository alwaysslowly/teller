package com.study.teller.common;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {

    /** 평문 → 해시 */
    public static String encode(String raw) {
        if (raw == null) raw = "";
        return BCrypt.withDefaults().hashToString(10, raw.toCharArray());
    }

    /** 평문과 해시 비교 */
    public static boolean matches(String raw, String hash) {
        if (raw == null || hash == null) return false;
        try {
            return BCrypt.verifyer().verify(raw.toCharArray(), hash.toCharArray()).verified;
        } catch (Exception e) {
            return false;
        }
    }
}