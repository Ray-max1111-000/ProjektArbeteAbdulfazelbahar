// The parent class for all saved items (WebLogin and WifiNetwork).
// It is abstract, so you can't create a plain "Credential" - only its child classes.
public abstract class Credential {

    // Every credential has a name and a password.
    protected String name;
    protected String password;

    public Credential(String name, String password) {
        this.name = name;
        this.password = password;
    }

    // Each child class must decide how to:
    public abstract String toCsv();  // turn itself into one line of text (for saving)
    public abstract void print();    // show itself on the screen
}
