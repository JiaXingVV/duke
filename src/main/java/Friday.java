import java.util.Scanner;

class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}
class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

class DukeException extends Exception {
    public DukeException(String message) {
        super(message);
    }
}
public class Friday {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println("____________________________________________________________");
        System.out.println("Hello! I'm Friday");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");
        System.out.println("\n");

        while (true) {
            String command = scanner.nextLine();
            System.out.println("____________________________________________________________");

            if (command.equalsIgnoreCase("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            } else if (command.equalsIgnoreCase("list")) {
                if (taskCount == 0) {
                    System.out.println("No tasks added yet.");
                    System.out.println("\n");
                } else {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                    System.out.println("\n");
                }
            } else {
                try {
                    String[] commandParts = command.split(" ", 2);
                    String taskType = commandParts[0].toLowerCase();
                    String taskDescription = commandParts.length > 1 ? commandParts[1] : "";

                    switch (taskType) {
                        case "mark":
                            int markIndex = Integer.parseInt(taskDescription) - 1;
                            if (markIndex >= 0 && markIndex < taskCount) {
                                tasks[markIndex].markAsDone();
                                System.out.println("Nice! I've marked this task as done:");
                                System.out.println("  " + tasks[markIndex]);
                                System.out.println("\n");
                            } else {
                                throw new DukeException("Invalid task index.");
                            }
                            break;
                        case "unmark":
                            int unmarkIndex = Integer.parseInt(taskDescription) - 1;
                            if (unmarkIndex >= 0 && unmarkIndex < taskCount) {
                                tasks[unmarkIndex].markAsNotDone();
                                System.out.println("OK, I've marked this task as not done yet:");
                                System.out.println("  " + tasks[unmarkIndex]);
                                System.out.println("\n");
                            } else {
                                throw new DukeException("Invalid task index.");
                            }
                            break;
                        case "todo":
                            if (taskDescription.isEmpty()) {
                                throw new DukeException("Please enter ToDo task.");
                            }
                            tasks[taskCount] = new Todo(taskDescription);
                            taskCount++;
                            System.out.println("Got it. I've added this task:");
                            System.out.println("  " + tasks[taskCount - 1]);
                            System.out.println("Now you have " + taskCount + " tasks in the list.");
                            System.out.println("\n");
                            break;
                        case "deadline":
                            if (taskDescription.isEmpty()) {
                                throw new DukeException("Invalid deadline format." +
                                        "Please use: deadline <task> /by <date>");
                            }
                            String[] deadlineParts = taskDescription.split("/by", 2);
                            String deadlineDescription = deadlineParts[0].trim();
                            String by = deadlineParts[1].trim();
                            tasks[taskCount] = new Deadline(deadlineDescription, by);
                            taskCount++;
                            System.out.println("Got it. I've added this task:");
                            System.out.println("  " + tasks[taskCount - 1]);
                            System.out.println("Now you have " + taskCount + " tasks in the list.");
                            System.out.println("\n");
                            break;
                        case "event":
                            String[] eventParts = taskDescription.split("/from", 2);
                            if (eventParts.length < 2) {
                                throw new DukeException("Invalid event format. Please use: " +
                                        "event <task> /from <start time> /to <end time>");
                            }
                            String eventDescription = eventParts[0].trim();
                            String[] eventTimeParts = eventParts[1].trim().split("/to", 2);
                            if (eventTimeParts.length < 2) {
                                throw new DukeException("Invalid event format. Please use: " +
                                        "event <task> /from <start time> /to <end time>");
                            }
                            String from = eventTimeParts[0].trim();
                            String to = eventTimeParts[1].trim();
                            tasks[taskCount] = new Event(eventDescription, from, to);
                            taskCount++;
                            System.out.println("Got it. I've added this task:");
                            System.out.println("  " + tasks[taskCount - 1]);
                            System.out.println("Now you have " + taskCount + " tasks in the list.");
                            System.out.println("\n");
                            break;
                        default:
                            throw new DukeException("I'm sorry, please give an invalid input:-(");
                    }
                } catch (DukeException e) {
                    System.out.println("OOPS!!! " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("OOPS!!! The task index must be a number.");
                    System.out.println("\n");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("OOPS!!! The task index is out of range.");
                    System.out.println("\n");
                }
            }
            System.out.println("____________________________________________________________");
        }
    }
}
