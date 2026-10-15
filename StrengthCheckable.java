// Interface for credentials whose password can be judged as weak.
// Each implementing class decides its own rule (polymorphism through the interface).
public interface StrengthCheckable {
    boolean hasWeakPassword();
}
