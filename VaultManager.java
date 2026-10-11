import java.util.*;

public class VaultManager {
    private List<Credential> vault = new ArrayList<>();
    private String file = "vault.dat", key;
    public VaultManager(String k) { key = k; }
    public void add(Credential c) { vault.add(c); }
    public void view() { vault.forEach(Credential::print); }
}
