import java.nio.file.*;
import java.util.*;

public class VaultManager {
    private List<Credential> vault = new ArrayList<>();
    private String file = "vault.dat", key;

    public VaultManager(String k) { key = k; }
    public void add(Credential c) { vault.add(c); }
    public void view() { vault.forEach(Credential::print); }
    
    public void save() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Credential c : vault) sb.append(c.toCsv()).append("\n");
        Files.writeString(Path.of(file), Crypto.process(sb.toString(), key, 1));
    }
    
    public boolean load() {
        try {
            if (!Files.exists(Path.of(file))) return true;
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
