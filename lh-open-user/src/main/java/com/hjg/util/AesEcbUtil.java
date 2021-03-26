package com.hjg.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class AesEcbUtil {
    // 加密:
    public static String encrypt(String key, String message){
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            byte[] input = message.getBytes();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey keySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(input));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("error occurred while encrypting: " + e.getMessage());
            return null;
        }
    }

    // 解密:
    public static String decrypt(String key, String secret){
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            byte[] input = Base64.getDecoder().decode(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey keySpec = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return new String(cipher.doFinal(input), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            log.error("error occurred while decrypting: " + e.getMessage());
            return null;
        }
    }

}
