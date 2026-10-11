// A website login. Inherits name + password from Credential, and adds a username.
public class WebLogin extends Credential {

    private String username;

    public WebLogin(String name, String username, String password) {
        super(name, password);
        this.username = username;
    }

    // Example: "WEB,Gmail,anna@gmail.com,secret123"
    @Override
    public String toCsv() {
        return "WEB," + name + "," + username + "," + password;
    }

    @Override
    public void print() {
        System.out.println("[WEB] " + name + " | User: " + username + " | Pass: " + password);
    }
}
