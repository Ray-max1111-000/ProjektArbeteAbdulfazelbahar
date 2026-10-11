public class WifiNetwork extends Credential {
    public WifiNetwork(String n, String p) { super(n, p); }
    public String toCsv() { return "WIFI," + name + "," + pass; }
    public void print() { System.out.println("[WIFI] SSID: " + name + " | Pass: " + pass); }
}
