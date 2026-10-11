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
    }
}
