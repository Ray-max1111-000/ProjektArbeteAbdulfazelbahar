import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

// Encrypts and decrypts text with AES, so vault.dat can't be read by others.
public class Crypto {

    // Scrambles readable text into secret text.
    public static String encrypt(String text, String key) throws Exception {
        Cipher cipher = createCipher(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);  // bytes -> safe text
    }

    // Turns secret text back into readable text.
    public static String decrypt(String encryptedText, String key) throws Exception {
        Cipher cipher = createCipher(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);  // text -> bytes
        return new String(cipher.doFinal(encryptedBytes));
    }

    // Sets up the AES engine. AES needs a key that is exactly 16 bytes long.
    private static Cipher createCipher(int mode, String key) throws Exception {
        byte[] keyBytes = Arrays.copyOf(key.getBytes(), 16);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(mode, secretKey);
        return cipher;
    }
}
