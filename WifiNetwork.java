// WifiNetwork inherits from Credential, representing a saved WiFi router password.
public class WifiNetwork extends Credential {
    
    // Constructor passes the SSID name and password up to the parent class.
    public WifiNetwork(String n, String p) { 
        super(n, p); 
    }
    
    // Formats the WiFi details into a comma-separated string for file storage.
    public String toCsv() { 
        return "WIFI," + name + "," + pass; 
    }
    
    // Prints formatted details of the WiFi network to the terminal.
    public void print() { 
        System.out.println("[WIFI] SSID: " + name + " | Pass: " + pass); 
    }
}
