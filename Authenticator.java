public class Authenticator {
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
