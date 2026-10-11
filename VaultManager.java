import java.nio.file.*;
import java.util.*;

// Manages your collection of credentials in memory and saves/loads them from the hard drive.
public class VaultManager {
    // An ArrayList acts as your digital container holding all loaded credentials in memory.
    private List<Credential> vault = new ArrayList<>();
    private String file = "vault.dat", key;

    // Constructor sets up the encryption key when the vault manager starts.
    public VaultManager(String k) { key = k; }
    
    // Adds a new credential object into the memory list.
    public void add(Credential c) { vault.add(c); }
    
    // Loops through every item in memory and prints its details to the screen.
    public void view() { vault.forEach(Credential::print); }
    
    // Encrypts all your credentials into a single text block and writes them permanently to vault.dat.
    public void save() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Credential c : vault) sb.append(c.toCsv()).append("\n");
        Files.writeString(Path.of(file), Crypto.process(sb.toString(), key, 1));
    }
    
    // Reads vault.dat from your computer, decrypts it, and recreates the objects in memory.
    public boolean load() {
        try {
            if (!Files.exists(Path.of(file))) return true; // If file doesn't exist yet, do nothing.
            String raw = Crypto.process(Files.readString(Path.of(file)), key, 2);
            for (String line : raw.split("\n")) {
                if(line.trim().isEmpty()) continue;
                String[] p = line.split(",");
                if (p[0].equals("WEB")) vault.add(new WebLogin(p[1], p[2], p[3]));
                if (p[0].equals("WIFI")) vault.add(new WifiNetwork(p[1], p[2]));
            }
            return true;
        } catch (Exception e) { return false; }
    }
}
