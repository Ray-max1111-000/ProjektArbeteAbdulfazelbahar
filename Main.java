import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter 2FA from Authenticator app: ");
        if (!Authenticator.verify(s.nextInt())) {
            System.out.println("Access Denied.");
            return;
        }
        s.nextLine(); 
        
        VaultManager v = new VaultManager("local_system_key_123");
        if (!v.load()) { 
            System.out.println("Corrupt file."); 
            return; 
        }
    }
}
