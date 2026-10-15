import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

// Checks the 6-digit code from Google/Microsoft Authenticator (TOTP).
// The phone and this program share a secret key. Both use it + the current time
// to calculate the same code. If the codes match, the user is allowed in.
public class Authenticator {

    // The shared secret. Read from the TOTP_SECRET environment variable, or use a test key.
    private static final String SECRET = System.getenv("TOTP_SECRET") != null
            ? System.getenv("TOTP_SECRET")
            : "JBSWY3DPEHPK3PXP";

    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    // Returns true if the code the user typed matches the code for right now.
    public static boolean verify(int userCode) throws GeneralSecurityException {
        return userCode == generateCurrentCode();
    }

    // Calculates the 6-digit code for the current 30-second window.
    private static int generateCurrentCode() throws GeneralSecurityException {
        // 1. Which 30-second window are we in? (changes every 30 seconds)
        long timeWindow = System.currentTimeMillis() / 1000 / 30;

        // 2. Mix the secret key and the time window with HMAC-SHA1.
        byte[] keyBytes = decodeBase32(SECRET);
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(keyBytes, "RawBytes"));
        byte[] hash = mac.doFinal(ByteBuffer.allocate(8).putLong(timeWindow).array());

        // 3. Pick 4 bytes from the hash and turn them into a number (standard TOTP rule).
        int offset = hash[hash.length - 1] & 0xF;
        int number = ((hash[offset]     & 0x7F) << 24)
                   | ((hash[offset + 1] & 0xFF) << 16)
                   | ((hash[offset + 2] & 0xFF) << 8)
                   |  (hash[offset + 3] & 0xFF);

        // 4. Keep only the last 6 digits.
        return number % 1_000_000;
    }

    // Converts the Base32 secret (letters A-Z and 2-7) into raw bytes.
    private static byte[] decodeBase32(String text) {
        byte[] result = new byte[text.length() * 5 / 8];
        int buffer = 0;
        int bitsInBuffer = 0;
        int index = 0;

        for (char c : text.toCharArray()) {
            buffer = (buffer << 5) | BASE32_ALPHABET.indexOf(c);  // each letter = 5 bits
            bitsInBuffer += 5;

            if (bitsInBuffer >= 8) {                               // enough for one byte
                result[index++] = (byte) (buffer >> (bitsInBuffer - 8));
                bitsInBuffer -= 8;
            }
        }
        return result;
    }
}
