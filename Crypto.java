import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

// Handles security by scrambling (encrypting) and unscrambling (decrypting) your data.
public class Crypto {
    
    // A single unified method to either encrypt or decrypt text using AES.
    public static String process(String data, String key, int mode) throws Exception {
        // Takes your secret key and ensures it is exactly 16 bytes long, required for standard AES.
        SecretKeySpec secret = new SecretKeySpec(Arrays.copyOf(key.getBytes(), 16), "AES");
        
        // Gets an instance of the AES encryption engine from Java.
        Cipher c = Cipher.getInstance("AES");
        
        // Initializes the cipher with the mode (Encrypt or Decrypt) and your secret key.
        c.init(mode, secret);
        
        // If mode is set to encrypt, scramble the text and encode it into readable Base64 characters.
        if (mode == Cipher.ENCRYPT_MODE) {
            return Base64.getEncoder().encodeToString(c.doFinal(data.getBytes()));
        }
        
        // Otherwise, decode the Base64 text and unscramble it back into readable plain text.
        return new String(c.doFinal(Base64.getDecoder().decode(data)));
    }
}
