// A WiFi network. The name is the network name (SSID).
public class WifiNetwork extends Credential {

    public WifiNetwork(String networkName, String password) {
        super(networkName, password);
    }

    // Example: "WIFI,HomeWiFi,mypassword"
    @Override
    public String toCsv() {
        return "WIFI," + name + "," + password;
    }

    @Override
    public void print() {
        System.out.println("[WIFI] SSID: " + name + " | Pass: " + password);
    }
}
