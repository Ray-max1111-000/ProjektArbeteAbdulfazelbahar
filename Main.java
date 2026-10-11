import java.util.Scanner;

// The program starts here. It shows a menu in the terminal and reacts to your choices.
public class Main {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        // Step 1: Check the 2FA code from the phone app.
        if (!checkTwoFactorCode()) {
            System.out.println("Access Denied.");
            return;
        }

        // Step 2: Open the vault and load saved passwords.
        VaultManager vault = new VaultManager("local_system_key_123");
        if (!vault.load()) {
            System.out.println("Corrupt file.");
            return;
        }

        // Step 3: Show the menu until the user chooses "Save & Exit".
        boolean running = true;
        while (running) {
            int choice = askForMenuChoice();

            if (choice == 1) {
                addWebLogin(vault);
            } else if (choice == 2) {
                addWifiNetwork(vault);
            } else if (choice == 3) {
                vault.view();
            } else if (choice == 4) {
                vault.save();
                running = false;
            }
        }
    }

    // Asks for the 6-digit code and checks it.
    private static boolean checkTwoFactorCode() throws Exception {
        System.out.print("Enter 2FA from Authenticator app: ");
        int code = input.nextInt();
        input.nextLine();
        return Authenticator.verify(code);
    }

    // Prints the menu and returns the number the user typed.
    private static int askForMenuChoice() {
        System.out.print("1:Add Web  2:Add WiFi  3:View  4:Save & Exit\n> ");
        int choice = input.nextInt();
        input.nextLine();
        return choice;
    }

    // Choice 1: Add a website login.
    private static void addWebLogin(VaultManager vault) {
        System.out.print("Name, User, Pass (comma separated): ");
        String[] parts = input.nextLine().split(",");

        String name     = parts[0];
        String username = parts[1];
        String password = parts[2];

        vault.add(new WebLogin(name, username, password));
    }

    // Choice 2: Add a WiFi network.
    private static void addWifiNetwork(VaultManager vault) {
        System.out.print("SSID, Pass (comma separated): ");
        String[] parts = input.nextLine().split(",");

        String networkName = parts[0];
        String password    = parts[1];

        vault.add(new WifiNetwork(networkName, password));
    }
}
