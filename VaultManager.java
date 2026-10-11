import java.util.*;

public class VaultManager {
    private List<Credential> vault = new ArrayList<>();
    private String file = "vault.dat", key;
    public VaultManager(String k) { key = k; }
}
