package com.study.teller.common;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

    /** 암호화 키 (학습용 - 실무는 설정파일이나 키관리 시스템에서) */
    private static final String KEY = "TellerSecretKey1";      // 16바이트
    private static final String IV  = "TellerInitVector";      // 16바이트

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";


    /** 암호화 */
    public static String encrypt(String plain) {
        try {
            if (plain == null) return null;

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,
                        new SecretKeySpec(KEY.getBytes("UTF-8"), "AES"),
                        new IvParameterSpec(IV.getBytes("UTF-8")));

            byte[] encrypted = cipher.doFinal(plain.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException("암호화 실패", e);
        }
    }


    /** 복호화 */
    public static String decrypt(String encoded) {
        try {
            if (encoded == null) return null;

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,
                        new SecretKeySpec(KEY.getBytes("UTF-8"), "AES"),
                        new IvParameterSpec(IV.getBytes("UTF-8")));

            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encoded));
            return new String(decrypted, "UTF-8");

        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }
}