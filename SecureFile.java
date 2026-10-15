import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;

// An image or video protected with its own password.
// The encrypted copy is saved as vault_files/<name>.enc. The original is never deleted.
public class SecureFile extends Credential implements StrengthCheckable {

    private static final Path FOLDER = Path.of("vault_files");
    private final Path original;

    public SecureFile(Path original, String password) {
        super(original.getFileName().toString(), password);
        this.original = original.toAbsolutePath();
    }

    // shortcut: reads the whole file into memory, fine up to about 1 GB; stream it if bigger files are needed.
    public void encrypt() throws IOException, GeneralSecurityException {
        String type = Files.probeContentType(original);
        if (type == null || !(type.startsWith("image/") || type.startsWith("video/"))) {
            throw new IllegalArgumentException("Only images and videos are supported: " + name);
        }
        Files.createDirectories(FOLDER);
        Files.write(encryptedPath(), Crypto.encrypt(Files.readAllBytes(original), password));
    }

    // Writes "decrypted_<name>" next to the original and returns where it went.
    public Path decrypt(String typedPassword) throws IOException, GeneralSecurityException {
        byte[] data = Crypto.decrypt(Files.readAllBytes(encryptedPath()), typedPassword);
        Path target = original.resolveSibling("decrypted_" + name);
        Files.write(target, data);
        return target;
    }

    private Path encryptedPath() {
        return FOLDER.resolve(name + ".enc");
    }

    @Override
    public String getType() {
        return "FILE";
    }

    // Example: "FILE,secret1234,/Users/x/holiday.mp4"
    @Override
    public String toCsv() {
        return "FILE," + password + "," + original;
    }

    @Override
    public String describe() {
        return "[FILE] " + name + " | " + original + " | Pass: " + password;
    }

    // Files can be attacked offline, so we ask for at least 10 characters.
    @Override
    public boolean hasWeakPassword() {
        return password.length() < 10;
    }
}
