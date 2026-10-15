// A website login. Adds a username to the inherited name and password.
public class WebLogin extends Credential implements StrengthCheckable {

    private final String username;

    public WebLogin(String name, String username, String password) {
        super(name, password);
        requireText("Username", username);
        this.username = username.trim();
    }

    @Override
    public String getType() {
        return "WEB";
    }

    // Example: "WEB,Gmail,anna@gmail.com,secret123"
    @Override
    public String toCsv() {
        return "WEB," + name + "," + username + "," + password;
    }

    @Override
    public String describe() {
        return "[WEB]  " + name + " | User: " + username + " | Pass: " + password;
    }

    // Weak: shorter than 8 characters or no digit.
    @Override
    public boolean hasWeakPassword() {
        return password.length() < 8 || !password.matches(".*\\d.*");
    }
}
