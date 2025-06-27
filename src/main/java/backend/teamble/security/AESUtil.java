package backend.teamble.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
    private static final String KEY = "0123456789abcdef"; // 16바이트 고정 키

    public static String encrypt(String plainText) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public static String decrypt(String cipherText) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }
}