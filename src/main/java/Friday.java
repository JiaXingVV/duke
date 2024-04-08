import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
    protected LocalDateTime by;

    public Deadline(String description, String by) {
        super(description);
        this.by = parseDateTime(by);
    }

    private LocalDateTime parseDateTime(String by) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
            return LocalDateTime.parse(by, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format. Please use: d/M/yyyy HHmm, e.g., 2/12/2019 1800");
        }
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a")) + ")";
    }
}
class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = LocalDateTime.parse(from, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
        this.to = LocalDateTime.parse(to, DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a"))
                + " to: " + to.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a")) + ")";
    }
}

class DukeException extends Exception {
    public DukeException(String message) {
        super(message);
    }
}


public class Friday {

    private static final String FILE_PATH = "./data/duke.txt";
    private static List<Task> tasks = new ArrayList<>();
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        loadTasks();

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
            } 
            else if (command.equalsIgnoreCase("list")) {
                if (tasks.isEmpty()) {
                    System.out.println("No tasks added yet.");
                    System.out.println("\n");
                } else {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                    System.out.println("\n");
                }
            } 
            else {
                try {
                    String[] commandParts = command.split(" ", 2);
                    String taskType = commandParts[0].toLowerCase();
                    String taskDescription = commandParts.length > 1 ? commandParts[1] : "";

                    switch (taskType) {
                        case "mark":
                            int markIndex = Integer.parseInt(taskDescription) - 1;
                            if (markIndex >= 0 && markIndex < tasks.size()) {
                                tasks.get(markIndex).markAsDone();
                                System.out.println("Nice! I've marked this task as done:");
                                System.out.println("  " + tasks.get(markIndex));
                                System.out.println("\n");
                            } else {
                                throw new DukeException("Invalid task index.");
                            }
                            break;
                        case "unmark":
                            int unmarkIndex = Integer.parseInt(taskDescription) - 1;
                            if (unmarkIndex >= 0 && unmarkIndex < tasks.size()) {
                                tasks.get(unmarkIndex).markAsNotDone();
                                System.out.println("OK, I've marked this task as not done yet:");
                                System.out.println("  " + tasks.get(unmarkIndex));
                                System.out.println("\n");
                            } else {
                                throw new DukeException("Invalid task index.");
                            }
                            break;
                        case "todo":
                            if (taskDescription.isEmpty()) {
                                throw new DukeException("Please enter ToDo task.");
                            }
                            Todo newTodo = new Todo(taskDescription);
                            if (!isDuplicate(newTodo)) {
                                tasks.add(newTodo);
                                System.out.println("Got it. I've added this task:");
                                System.out.println("  " + newTodo);
                                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                            } else {
                                System.out.println("This task is already in your list.");
                            }
                            System.out.println("\n");
                            break;
                        case "deadline":
                            String[] deadlineParts = taskDescription.split("/by", 2);
                            if (deadlineParts.length < 2) {
                                throw new DukeException("Invalid deadline format. Please use: " +
                                        "deadline <task> /by <date>");
                            }
                            String deadlineDescription = deadlineParts[0].trim();
                            String by = deadlineParts[1].trim();
                            Deadline newDeadline = new Deadline(deadlineDescription, by);
                            if (!isDuplicate(newDeadline)) {
                                tasks.add(newDeadline);
                                System.out.println("Got it. I've added this task:");
                                System.out.println("  " + newDeadline);
                                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                            } else {
                                System.out.println("This deadline is already in your list.");
                            }
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
                            Event newEvent = new Event(eventDescription, from, to);
                            if (!isDuplicate(newEvent)) {
                                tasks.add(newEvent);
                                System.out.println("Got it. I've added this event:");
                                System.out.println("  " + newEvent);
                                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                            } else {
                                System.out.println("This event is already in your list.");
                            }
                            System.out.println("\n");
                            break;

                        case "delete":
                            int deleteIndex = Integer.parseInt(taskDescription) - 1;
                            if (deleteIndex >= 0 && deleteIndex < tasks.size()) {
                                Task deletedTask = tasks.remove(deleteIndex);
                                System.out.println("Noted. I've removed this task:");
                                System.out.println("  " + deletedTask);
                                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                            } else {
                                throw new DukeException("Invalid task index.");
                            }
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
            saveTasks();
            System.out.println("____________________________________________________________");
        }
    }
    private static void loadTasks() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                return;
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" \\| ");
                Task task = null;
                switch (parts[0]) {
                    case "T":
                        task = new Todo(parts[2]);
                        break;
                    case "D":
                        task = new Deadline(parts[2], parts[3]);
                        break;
                    case "E":
                        // Assuming Event class also uses LocalDateTime
                        task = new Event(parts[2], parts[3], parts[4]);
                        break;
                }
                if (task != null) {
                    if (parts[1].equals("1")) {
                        task.markAsDone();
                    }
                    tasks.add(task);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Starting with an empty task list.");
        }
    }
    

    private static void saveTasks() {
        try {
            PrintWriter writer = new PrintWriter(FILE_PATH);
            for (Task task : tasks) {
                writer.println(taskToFileString(task));
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error saving tasks. Data file not found.");
        }
    }

    private static String taskToFileString(Task task) {
        String type = "";
        String timeInfo = "";
    
        if (task instanceof Todo) {
            type = "T";
        } else if (task instanceof Deadline) {
            type = "D";
            timeInfo = " | " + ((Deadline) task).by.format(DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));
        } else if (task instanceof Event) {
            // Assuming Event class also uses LocalDateTime
            type = "E";
            timeInfo = " | " + ((Event) task).from.format(DateTimeFormatter.ofPattern("d/M/yyyy HHmm"))
                    + " | " + ((Event) task).to.format(DateTimeFormatter.ofPattern("d/M/yyyy HHmm"));


        }
    
        return type + " | " + (task.isDone ? "1" : "0") + " | " + task.description + timeInfo;
    }
    private static boolean isDuplicate(Task newTask) {
        for (Task task : tasks) {
            if (task.toString().equals(newTask.toString())) {
                return true;
            }
        }
        return false;
    }
}

