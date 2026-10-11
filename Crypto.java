import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Arrays;

public class Crypto {
    public static String process(String data, String key, int mode) throws Exception {
        SecretKeySpec secret = new SecretKeySpec(Arrays.copyOf(key.getBytes(), 16), "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(mode, secret);
        if (mode == Cipher.ENCRYPT_MODE) {
            return Base64.getEncoder().encodeToString(c.doFinal(data.getBytes()));
        }
        return new String(c.doFinal(Base64.getDecoder().decode(data)));
    }
}
