import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Keeps all credentials in a list, and saves/loads them to an encrypted file.
public class VaultManager {

    private static final Path VAULT_FILE = Path.of("vault.dat");

    private final List<Credential> credentials = new ArrayList<>();
    private final String encryptionKey;

    public VaultManager(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    // Adds one credential to the list.
    public void add(Credential credential) {
        credentials.add(credential);
    }

    // Prints every credential on the screen.
    public void view() {
        for (Credential credential : credentials) {
            credential.print();
        }
    }

    // Turns all credentials into text, encrypts it, and writes it to vault.dat.
    public void save() throws Exception {
        StringBuilder text = new StringBuilder();
        for (Credential credential : credentials) {
            text.append(credential.toCsv()).append("\n");
        }

        String encrypted = Crypto.encrypt(text.toString(), encryptionKey);
        Files.writeString(VAULT_FILE, encrypted);
    }

    // Reads vault.dat, decrypts it, and rebuilds the credentials.
    // Returns false if the file could not be read (for example, wrong key).
    public boolean load() {
        // First time running: no file yet, so there is nothing to load.
        if (!Files.exists(VAULT_FILE)) {
            return true;
        }

        try {
            String encrypted = Files.readString(VAULT_FILE);
            String text = Crypto.decrypt(encrypted, encryptionKey);

            for (String line : text.split("\n")) {
                if (!line.isBlank()) {
                    credentials.add(createFromCsv(line));
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Turns one saved line back into the right type of credential.
    //   "WEB,name,user,pass"  -> WebLogin
    //   "WIFI,name,pass"      -> WifiNetwork
    private Credential createFromCsv(String line) {
        String[] parts = line.split(",");
        String type = parts[0];

        if (type.equals("WEB")) {
            return new WebLogin(parts[1], parts[2], parts[3]);
        }
        return new WifiNetwork(parts[1], parts[2]);
    }
}
