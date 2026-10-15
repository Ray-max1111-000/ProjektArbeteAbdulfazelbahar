// A WiFi network. The inherited name is the network name (SSID).
public class WifiNetwork extends Credential implements StrengthCheckable {

    public WifiNetwork(String networkName, String password) {
        super(networkName, password);
    }

    @Override
    public String getType() {
        return "WIFI";
    }

    // Example: "WIFI,HomeWiFi,mypassword"
    @Override
    public String toCsv() {
        return "WIFI," + name + "," + password;
    }

    @Override
    public String describe() {
        return "[WIFI] SSID: " + name + " | Pass: " + password;
    }

    // Weak: WPA2 requires at least 8 characters.
    @Override
    public boolean hasWeakPassword() {
        return password.length() < 8;
    }
}
