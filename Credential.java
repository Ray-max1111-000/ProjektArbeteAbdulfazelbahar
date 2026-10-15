// Abstract superclass for everything stored in the vault.
// Subclasses: WebLogin, WifiNetwork, SecureFile.
// It cannot be instantiated directly; each subclass overrides getType(), toCsv() and describe().
public abstract class Credential {

    protected final String name;
    protected final String password;

    public Credential(String name, String password) {
        requireText("Name", name);
        requireText("Password", password);
        this.name = name.trim();
        this.password = password;
    }

    // Validation shared by all subclasses. A comma would break the CSV format in vault.dat.
    protected static void requireText(String field, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be empty.");
        }
        if (value.contains(",")) {
            throw new IllegalArgumentException(field + " cannot contain a comma (,).");
        }
    }

    public String getName() {
        return name;
    }

    public abstract String getType();   // short label, e.g. "WEB"
    public abstract String toCsv();     // one line of text for vault.dat
    public abstract String describe();  // human-readable text for the menu
}
