import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

// Encrypts and decrypts bytes with AES. Used for both vault.dat and images/videos.
// Output layout: [16-byte random IV][encrypted bytes]
public class Crypto {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public static byte[] encrypt(byte[] data, String password) throws GeneralSecurityException {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        byte[] encrypted = cipher(Cipher.ENCRYPT_MODE, password, iv).doFinal(data);

        byte[] result = Arrays.copyOf(iv, 16 + encrypted.length);
        System.arraycopy(encrypted, 0, result, 16, encrypted.length);
        return result;
    }

    // A wrong password or damaged data throws BadPaddingException.
    public static byte[] decrypt(byte[] data, String password) throws GeneralSecurityException {
        byte[] iv = Arrays.copyOfRange(data, 0, 16);
        return cipher(Cipher.DECRYPT_MODE, password, iv).doFinal(data, 16, data.length - 16);
    }

    // shortcut: key = SHA-256 of the password; use PBKDF2 with a salt in a real product.
    private static Cipher cipher(int mode, String password, byte[] iv) throws GeneralSecurityException {
        byte[] key = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        return cipher;
    }
}
