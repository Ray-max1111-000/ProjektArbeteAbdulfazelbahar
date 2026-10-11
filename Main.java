import java.util.Scanner;

// The core entry point where the program starts running and interacts with you in the terminal.
public class Main {
    public static void main(String[] args) throws Exception {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter 2FA from Authenticator app: ");
        
        // Verifies the code from your phone app before allowing access.
        if (!Authenticator.verify(s.nextInt())) {
            System.out.println("Access Denied.");
            return;
        }
        s.nextLine(); 
        
        // Initializes the vault manager with a system security key.
        VaultManager v = new VaultManager("local_system_key_123");
        if (!v.load()) { 
            System.out.println("Corrupt file."); 
            return; 
        }
        
        // Continuous loop keeping the program open until you choose to exit.
        while (true) {
            System.out.print("1:Add Web  2:Add WiFi  3:View  4:Save & Exit\n> ");
            int c = s.nextInt(); s.nextLine();
            
            // Choice 1: Add a website login.
            if (c == 1) {
                System.out.print("Name, User, Pass (comma separated): ");
                String[] in = s.nextLine().split(",");
                v.add(new WebLogin(in[0], in[1], in[2]));
            } 
            // Choice 2: Add a WiFi network.
            else if (c == 2) {
                System.out.print("SSID, Pass (comma separated): ");
                String[] in = s.nextLine().split(",");
                v.add(new WifiNetwork(in[0], in[1]));
            } 
            // Choice 3: View all items currently saved in the vault.
            else if (c == 3) {
                v.view();
            } 
            // Choice 4: Encrypt, save to disk, and shutdown the program.
            else if (c == 4) {
                v.save(); 
                break;
            }
        }
    }
}
