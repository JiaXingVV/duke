import java.util.Scanner;
public class Friday {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-------------------------------------------------");
        System.out.println("Hello! I'm Friday");
        System.out.println("What can I do for you?");
        System.out.println("-------------------------------------------------");
        while (true) {
            String command = scanner.nextLine();
            System.out.println("____________________________________________________________");
            System.out.println(command);
            System.out.println("____________________________________________________________");
            if (command.equalsIgnoreCase("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }
        }
    }
}
