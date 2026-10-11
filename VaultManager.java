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
}
