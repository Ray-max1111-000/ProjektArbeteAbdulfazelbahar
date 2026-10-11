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
        
        while (true) {
            System.out.print("1:Add Web  2:Add WiFi  3:View  4:Save & Exit\n> ");
            int c = s.nextInt(); s.nextLine();
            
            if (c == 1) {
                System.out.print("Name, User, Pass (comma separated): ");
                String[] in = s.nextLine().split(",");
                v.add(new WebLogin(in[0], in[1], in[2]));
            } else if (c == 2) {
                System.out.print("SSID, Pass (comma separated): ");
                String[] in = s.nextLine().split(",");
                v.add(new WifiNetwork(in[0], in[1]));
            }
        }
    }
}
