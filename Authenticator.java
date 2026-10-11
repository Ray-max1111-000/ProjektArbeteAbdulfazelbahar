import java.nio.ByteBuffer;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

// Handles Time-based One-Time Password (TOTP) verification for Google/Microsoft Authenticator apps.
public class Authenticator {
    // Grabs your secret from the system environment variable, or falls back to a default test key.
    public static final String SECRET = System.getenv("TOTP_SECRET") != null ? System.getenv("TOTP_SECRET") : "JBSWY3DPEHPK3PXP";
    
    // Checks if the 6-digit number typed by the user matches what the phone app generates right now.
    public static boolean verify(int input) throws Exception {
        // Calculates the current 30-second time window block based on system clock milliseconds.
        long time = System.currentTimeMillis() / 1000 / 30;
        
        // Decodes the text secret key into raw bytes for math operations.
        byte[] decoded = decodeBase32(SECRET);
        
        // Uses HMAC-SHA1 cryptographic hashing to combine the secret key and the time window.
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(decoded, "RawBytes"));
        byte[] hash = mac.doFinal(ByteBuffer.allocate(8).putLong(time).array());
        
        // Extracts a clean 6-digit number from the resulting cryptographic hash.
        int offset = hash[hash.length - 1] & 0xF;
        int code = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16) | 
                   ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);
                   
        // Compares the user input code with the mathematically expected code.
        return input == (code % 1000000);
    }
    
    // Helper method to decode Base32 formatted secrets into standard byte arrays.
    private static byte[] decodeBase32(String s) {
        byte[] b = new byte[s.length() * 5 / 8];
        int buf = 0, bits = 0, i = 0;
        for (char c : s.toCharArray()) {
            buf = (buf << 5) | "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".indexOf(c);
            bits += 5;
            if (bits >= 8) { b[i++] = (byte) (buf >> (bits - 8)); bits -= 8; }
        }
        return b;
    }
}
