// WebLogin inherits from the Credential class, representing a website login.
public class WebLogin extends Credential {
    // Specific field unique to web logins (username or email).
    private String user;
    
    // Constructor passes name and password up to the parent Credential class, and saves the user.
    public WebLogin(String n, String u, String p) { 
        super(n, p); 
        this.user = u; 
    }
    
    // Converts the object data into a plain text comma-separated line for saving to a file.
    public String toCsv() { 
        return "WEB," + name + "," + user + "," + pass; 
    }
    
    // Prints formatted details of the website login directly to the terminal.
    public void print() { 
        System.out.println("[WEB] " + name + " | User: " + user + " | Pass: " + pass); 
    }
}
