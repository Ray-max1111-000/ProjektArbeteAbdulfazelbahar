import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

public class Authenticator {
    public static final String SECRET = "JBSWY3DPEHPK3PXP";
    
    public static boolean verify(int input) throws Exception {
        long time = System.currentTimeMillis() / 1000 / 30;
        byte[] decoded = decodeBase32(SECRET);
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(decoded, "RawBytes"));
        byte[] hash = mac.doFinal(ByteBuffer.allocate(8).putLong(time).array());
        int offset = hash[hash.length - 1] & 0xF;
        int code = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16) | 
                   ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);
        return input == (code % 1000000);
    }
    
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
