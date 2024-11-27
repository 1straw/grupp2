package se.dsve.ui;
import org.hibernate.SessionFactory;
import se.dsve.config.DatabaseConnection;
import se.dsve.config.H2Connection;
import se.dsve.config.MySQLConnection;
import se.dsve.dao.TodoDAO;
import se.dsve.dao.impl.TodoDAOImpl;
import se.dsve.entity.Todo;
import se.dsve.service.TodoService;

import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MenuHandler {
    private final Scanner scanner;
    private final TodoService todoService;

    public MenuHandler(TodoService todoService) {
        this.scanner = new Scanner(System.in);
        this.todoService = todoService;
    }

    /*
    public void chooseDatabase() {
        System.out.println("Välj databasanslutning:");
        System.out.println("1. H2");
        System.out.println("2. MySQL");
        System.out.println("3. Avsluta");
        System.out.print("Ange ditt val: ");

        int choice = scanner.nextInt();
        scanner.nextLine();
        boolean running = true;
        DatabaseConnection connection = null;

        while (running) {
            switch (choice) {
                case 1 -> connection = new H2Connection();
                case 2 -> connection = new MySQLConnection();
                case 3 -> {
                    System.out.println("Exiting...");
                    running = false;
                }
                default -> System.out.println("Ogiltigt val, välj igen.");
            }
        }
        return connection;
    }
    */

    boolean running = true;
    public void displayMenu() {
        while (running) {
            System.out.println("\n--- Todo applikation ---");
            System.out.println("1. Lägg till ny todo ");
            System.out.println("2. Visa alla todos");
            System.out.println("3. Markera todo som klar");
            System.out.println("4. Ta bort todo ");
            System.out.println("5. Avsluta ");
            System.out.println("Ange ett alternativ: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> add();
                case 2 -> getAll();
                case 3 -> update();
                case 4 -> delete();
                case 5 -> {
                    System.out.println("Exiting...");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void add() {
        System.out.println("Lägg till ny todo: ");
        String description = scanner.nextLine();

        Todo todo = new Todo(description);
        todoService.addTodo(todo);
        System.out.println("Todo tillagd.");
    }

    private void getAll() {
        List<Todo> todos = todoService.getAllTodo();
        if (todos.isEmpty()) {
            System.out.println("Inga todos finns.");
        } else {
            System.out.println("Alla todos:");
            for (Todo todo : todos) {
                System.out.println(todo.getId() + ": " + todo.getDescription() + ", " + todo.getCreatedAt() + ", färdig: " + todo.isCompleted());
            }
        }
    }

    private void update() {
        todoService.getAllTodo();
        System.out.print("Ange todo ID för att uppdatera: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Todo> optionalTodo = todoService.getTodoById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();

            todo.setCompleted(true);
            todoService.updateTodo(todo);
            System.out.println("Todo uppdaterad: " + todo.getDescription());
        } else {
            System.out.println("Kunde inte uppdatera todo.");
        }
    }

    private void delete() {
        System.out.print("Ange todo ID för att radera: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Optional<Todo> optionalTodo = todoService.getTodoById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            todoService.deleteTodo(todo);
            System.out.println("Todo raderad.");
        } else {
            System.out.println("Todo med ID: " + id + "hittades inte.");
        }

    }
}
