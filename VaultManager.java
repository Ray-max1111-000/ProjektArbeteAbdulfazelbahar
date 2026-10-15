import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// Holds all credentials and the rules for them: add, search, remove, statistics, save and load.
public class VaultManager {

    private static final Path VAULT_FILE = Path.of("vault.dat");
    private final List<Credential> credentials = new ArrayList<>();
    private final String key;

    public VaultManager(String key) {
        this.key = key;
    }

    // Names are unique, so one entry can always be found by its name.
    public void add(Credential credential) {
        if (find(credential.getName()) != null) {
            throw new IllegalArgumentException("'" + credential.getName() + "' already exists.");
        }
        credentials.add(credential);
    }

    // Encrypts first and adds afterwards, so a failed encryption leaves no broken entry.
    public void addFile(SecureFile file) throws IOException, GeneralSecurityException {
        if (find(file.getName()) != null) {
            throw new IllegalArgumentException("'" + file.getName() + "' already exists.");
        }
        file.encrypt();
        credentials.add(file);
    }

    public List<Credential> getAll() {
        return credentials;
    }

    public Credential find(String name) {
        for (Credential c : credentials) {
            if (c.getName().equalsIgnoreCase(name.trim())) {
                return c;
            }
        }
        return null;
    }

    public List<Credential> search(String text) {
        List<Credential> matches = new ArrayList<>();
        for (Credential c : credentials) {
            if (c.getName().toLowerCase().contains(text.trim().toLowerCase())) {
                matches.add(c);
            }
        }
        return matches;
    }

    public boolean remove(String name) {
        return credentials.remove(find(name));
    }

    // Example: {FILE=2, WEB=3, WIFI=1}
    public Map<String, Integer> countByType() {
        Map<String, Integer> counts = new TreeMap<>();
        for (Credential c : credentials) {
            counts.merge(c.getType(), 1, Integer::sum);
        }
        return counts;
    }

    // Polymorphism through the interface: each class has its own rule for "weak".
    public List<Credential> weakPasswords() {
        List<Credential> weak = new ArrayList<>();
        for (Credential c : credentials) {
            if (c instanceof StrengthCheckable && ((StrengthCheckable) c).hasWeakPassword()) {
                weak.add(c);
            }
        }
        return weak;
    }

    public void save() throws IOException, GeneralSecurityException {
        StringBuilder text = new StringBuilder();
        for (Credential c : credentials) {
            text.append(c.toCsv()).append("\n");
        }
        Files.write(VAULT_FILE, Crypto.encrypt(text.toString().getBytes(), key));
    }

    public void load() throws IOException, GeneralSecurityException {
        if (!Files.exists(VAULT_FILE)) {
            return;  // first run
        }
        String text = new String(Crypto.decrypt(Files.readAllBytes(VAULT_FILE), key));
        for (String line : text.split("\n")) {
            if (!line.isBlank()) {
                credentials.add(fromCsv(line.split(",")));
            }
        }
    }

    // A new credential type only needs a new subclass and one new case here.
    private Credential fromCsv(String[] p) {
        try {
            switch (p[0]) {
                case "WEB":  return new WebLogin(p[1], p[2], p[3]);
                case "WIFI": return new WifiNetwork(p[1], p[2]);
                case "FILE": return new SecureFile(Path.of(p[2]), p[1]);
                default: throw new IllegalArgumentException("vault.dat has an unknown entry type: " + p[0]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("vault.dat has a damaged entry.");
        }
    }
}
