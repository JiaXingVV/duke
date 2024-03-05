import java.util.Scanner;
public class Friday {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
        int taskCount = 0;


        System.out.println("-------------------------------------------------");
        System.out.println("Hello! I'm Friday");
        System.out.println("What can I do for you?");
        System.out.println("-------------------------------------------------");


        while (true) {
            String command = scanner.nextLine();
            System.out.println("____________________________________________________________");
            if (command.equalsIgnoreCase("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }
            else if (command.equalsIgnoreCase("list")) {
                if (taskCount == 0) {
                    System.out.println("No tasks");
                    System.out.println("\n");
                } else {
                    System.out.println("Tasks:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + ". " + tasks[i]);
                    }
                    System.out.println("\n");
                }
            }
            else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
                System.out.println("\n");
            }
        }
    }
}
