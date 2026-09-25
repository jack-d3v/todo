package jackd3v;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import jackd3v.domain.Status;
import jackd3v.domain.Task;
import jackd3v.domain.TaskRepository;
import jackd3v.infrastructure.HibernateTaskRepository;

public class App {

    private record Selection(Task task, String message) {
    }

    void main() throws Exception {
        java.util.logging.Logger.getLogger("org.hibernate")
                .setLevel(java.util.logging.Level.WARNING);

        SessionFactory sessionFactory = new Configuration().addAnnotatedClass(Task.class).buildSessionFactory();

        try (sessionFactory) {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            TaskRepository repository = new HibernateTaskRepository(sessionFactory);
            Status filter = null;
            String message = "";

            loop: while (true) {
                System.out.print("\033[2J\033[H");

                List<Task> tasks = repository.findAll();

                Status current = filter;
                if (current != null) {
                    tasks = tasks.stream().filter(t -> t.getStatus() == current).toList();
                }

                System.out.println("to-do list  (filter: " + (filter == null ? "all" : filter) + ")");
                System.out.println();

                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.get(i);
                    System.out.println(
                            (i + 1) + ". [" + task.getStatus().getSymbol() + "] " + task.getContent());
                }

                if (!message.isBlank()) {
                    System.out.println();
                    System.out.println(message);
                }
                message = "";

                System.out.println();
                System.out.print("> ");
                System.out.flush();

                String line = input.readLine();

                if (line == null) {
                    break;
                }

                if (line.isBlank()) {
                    continue;
                }

                String[] parts = line.trim().split("\\s+", 2);
                String command = parts[0];
                String argument = parts.length > 1 ? parts[1] : "";

                switch (command) {
                    case "quit" -> {
                        break loop;
                    }
                    case "add" -> {
                        if (argument.isBlank()) {
                            message = "usage: add <task text>";
                        } else {
                            repository.save(new Task(argument));
                        }
                    }
                    case "close" -> {
                        Selection selection = selectTask(argument, tasks);
                        if (selection.task() == null) {
                            message = selection.message();
                        } else {
                            selection.task().close();
                            repository.update(selection.task());
                        }
                    }
                    case "park" -> {
                        Selection selection = selectTask(argument, tasks);
                        if (selection.task() == null) {
                            message = selection.message();
                        } else {
                            selection.task().park();
                            repository.update(selection.task());
                        }
                    }
                    case "activate" -> {
                        Selection selection = selectTask(argument, tasks);
                        if (selection.task() == null) {
                            message = selection.message();
                        } else {
                            selection.task().activate();
                            repository.update(selection.task());
                        }
                    }
                    case "edit" -> {
                        String[] editParts = argument.split("\\s+", 2);
                        String newText = editParts.length > 1 ? editParts[1] : "";

                        if (newText.isBlank()) {
                            message = "usage: edit <task number> <new text>";
                        } else {
                            Selection selection = selectTask(editParts[0], tasks);
                            if (selection.task() == null) {
                                message = selection.message();
                            } else {
                                selection.task().reword(newText);
                                repository.update(selection.task());
                            }
                        }
                    }
                    case "filter" -> {
                        switch (argument) {
                            case "active" -> filter = Status.ACTIVE;
                            case "parked" -> filter = Status.PARKED;
                            case "closed" -> filter = Status.CLOSED;
                            case "all" -> filter = null;
                            default -> message = "filter: active, parked, closed or all";
                        }
                    }
                    default -> message = "unknown command: " + command;
                }
            }
        }
    }

    private static Selection selectTask(String argument, List<Task> tasks) {
        if (!argument.matches("\\d+")) {
            return new Selection(null, "usage: <command> <task number>");
        }

        int number = Integer.parseInt(argument);

        if (number < 1 || number > tasks.size()) {
            return new Selection(null, "no task " + number);
        }

        return new Selection(tasks.get(number - 1), "");
    }
}
