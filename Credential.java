// This is an abstract blueprint class. It cannot be used directly, 
// but it gives common rules and fields for specific types of credentials (like Web or WiFi).
public abstract class Credential {
    // Protected fields can be accessed by any class that inherits from Credential.
    protected String name, pass;
    
    // Constructor to initialize the name and password when a credential is created.
    public Credential(String name, String pass) { 
        this.name = name; 
        this.pass = pass; 
    }
    
    // Abstract methods force child classes to write their own versions of these behaviors.
    public abstract String toCsv();
    public abstract void print();
}
