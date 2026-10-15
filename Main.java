import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.List;
import javax.crypto.BadPaddingException;

// Console menu. Only asks and prints; all rules live in VaultManager and the Credential classes.
public class Main {

    private static final VaultManager vault = new VaultManager("local_system_key_123");

    public static void main(String[] args) throws GeneralSecurityException {
        if (!Authenticator.verify(ConsoleInput.number("2FA code: "))) {
            System.out.println("Access denied.");
            return;
        }
        try {
            vault.load();
        } catch (BadPaddingException e) {
            System.out.println("vault.dat is damaged or the key is wrong.");
            return;
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Could not open the vault: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("\n1 Add web  2 Add WiFi  3 Encrypt file  4 Decrypt file  5 View");
            System.out.println("6 Search  7 Remove  8 Statistics  9 Save & exit");
            int choice = ConsoleInput.number("> ");
            if (choice == 9 && save()) {
                return;
            }
            handle(choice);
        }
    }

    private static void handle(int choice) {
        try {
            switch (choice) {
                case 1: addWeb(); break;
                case 2: addWifi(); break;
                case 3: encryptFile(); break;
                case 4: decryptFile(); break;
                case 5: print(vault.getAll()); break;
                case 6: print(vault.search(ConsoleInput.text("Search: "))); break;
                case 7: remove(); break;
                case 8: statistics(); break;
                case 9: break;  // saving failed, stay in the menu
                default: System.out.println("Choose 1-9.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());  // our own validation messages
        } catch (NoSuchFileException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (BadPaddingException e) {
            System.out.println("Wrong password or damaged file.");
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (GeneralSecurityException e) {
            System.out.println("Encryption error: " + e.getMessage());
        }
    }

    private static void addWeb() {
        String name = ConsoleInput.text("Name: ");
        String user = ConsoleInput.text("Username: ");
        vault.add(new WebLogin(name, user, ConsoleInput.text("Password: ")));
        System.out.println("Added.");
    }

    private static void addWifi() {
        String ssid = ConsoleInput.text("SSID: ");
        vault.add(new WifiNetwork(ssid, ConsoleInput.text("Password: ")));
        System.out.println("Added.");
    }

    private static void encryptFile() throws IOException, GeneralSecurityException {
        Path path = Path.of(ConsoleInput.path("Drag an image or video here: "));
        vault.addFile(new SecureFile(path, ConsoleInput.text("Password for the file: ")));
        save();  // save now, so the file password is never lost
        System.out.println("Encrypted into vault_files/. The original was kept.");
    }

    private static void decryptFile() throws IOException, GeneralSecurityException {
        Credential entry = vault.find(ConsoleInput.text("File name: "));
        if (!(entry instanceof SecureFile)) {
            System.out.println("No encrypted file with that name.");
            return;
        }
        Path restored = ((SecureFile) entry).decrypt(ConsoleInput.text("Password: "));
        System.out.println("Decrypted to " + restored);
    }

    private static void remove() {
        boolean removed = vault.remove(ConsoleInput.text("Name to remove: "));
        System.out.println(removed ? "Removed." : "No entry with that name.");
    }

    private static void statistics() {
        System.out.println("Entries per type: " + vault.countByType());
        System.out.println("Weak passwords:");
        print(vault.weakPasswords());
    }

    // Polymorphism: each object runs its own describe().
    private static void print(List<Credential> list) {
        if (list.isEmpty()) {
            System.out.println("(nothing)");
        }
        for (Credential c : list) {
            System.out.println(c.describe());
        }
    }

    private static boolean save() {
        try {
            vault.save();
            return true;
        } catch (IOException | GeneralSecurityException e) {
            System.out.println("Could not save: " + e.getMessage());
            return false;
        }
    }
}
