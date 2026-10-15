import java.util.Scanner;

// Reads from the keyboard. Bad input (letters, empty answers) never crashes the program.
public class ConsoleInput {

    private static final Scanner scanner = new Scanner(System.in);

    public static String text(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static int number(String prompt) {
        while (true) {
            String answer = text(prompt).trim();
            try {
                return Integer.parseInt(answer);
            } catch (NumberFormatException e) {
                System.out.println("'" + answer + "' is not a number. Try again.");
            }
        }
    }

    // Dragging a file into the macOS terminal adds quotes or "\ " before spaces.
    public static String path(String prompt) {
        return text(prompt).trim().replace("'", "").replace("\"", "").replace("\\ ", " ");
    }
}
