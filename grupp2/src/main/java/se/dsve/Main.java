package se.dsve;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String DB_HOST = "localhost";
        String DB_PORT = "3306";
        String DB_NAME = "todo_db";
        String DB_USER = "root";
        String DB_PASSWORD = "password";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            return;
        }

        Connection conn = null;
        try {
            // Försök att ansluta till MySQL-servern utan att specificera databasen
            conn = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + ":" + DB_PORT, DB_USER, DB_PASSWORD);

            // Skapa databasen om den inte finns
            Statement createDbStmt = conn.createStatement();
            createDbStmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            createDbStmt.close();

            // Stäng den första anslutningen
            conn.close();

            // Anslut igen, nu med den specifika databasen
            String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            Statement createTableStmt = conn.createStatement();
            createTableStmt.execute("CREATE TABLE IF NOT EXISTS todos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "description VARCHAR(255) NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "completed BOOLEAN DEFAULT FALSE)");
            createTableStmt.close();

            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("\n╔════════════════════════════╗");
                System.out.println("║       TODO HANTERARE       ║");
                System.out.println("╠════════════════════════════╣");
                System.out.println("║ 1. Lägg till ny todo       ║");
                System.out.println("║ 2. Visa alla todos         ║");
                System.out.println("║ 3. Markera todo som klar   ║");
                System.out.println("║ 4. Ta bort todo            ║");
                System.out.println("║ 5. Avsluta                 ║");
                System.out.println("╚════════════════════════════╝");
                System.out.print("Välj ett alternativ: ");

                int choice = 0;
                while (!scanner.hasNextInt()) {
                    System.out.println("Vänligen ange ett nummer.");
                    scanner.next();
                }
                choice = scanner.nextInt();

                if (choice == 1) {
                    System.out.print("Ange todo-beskrivning: ");
                    scanner.nextLine(); // Konsumera newline
                    String description = scanner.nextLine();

                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO todos (description) VALUES (?)");
                    pstmt.setString(1, description);
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Todo tillagd framgångsrikt!");
                    }
                    pstmt.close();
                } else if (choice == 2) {
                    Statement selectStmt = conn.createStatement();
                    ResultSet rs = selectStmt.executeQuery("SELECT * FROM todos ORDER BY created_at DESC");

                    if (!rs.isBeforeFirst()) {
                        System.out.println("Inga todos att visa.");
                    } else {
                        System.out.println("\n╔════╦═══════════════════════════════════════╦════════════╦═══════════╗");
                        System.out.println("║ ID ║ Beskrivning                           ║ Skapad     ║ Status    ║");
                        System.out.println("╠════╬═══════════════════════════════════════╬════════════╬═══════════╣");

                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String description = rs.getString("description");
                            Timestamp createdAt = rs.getTimestamp("created_at");
                            boolean completed = rs.getBoolean("completed");

                            System.out.printf("║ %-2d ║ %-37s ║ %-10s ║ %-9s ║%n",
                                    id,
                                    description.length() > 37 ? description.substring(0, 34) + "..." : description,
                                    createdAt.toString().substring(0, 10),
                                    completed ? "Klar" : "Pågående"
                            );
                        }
                        System.out.println("╚════╩═══════════════════════════════════════╩════════════╩═══════════╝");
                    }
                    rs.close();
                    selectStmt.close();
                } else if (choice == 3) {
                    System.out.print("Ange ID för todo att markera som klar: ");
                    int id = scanner.nextInt();

                    PreparedStatement pstmt = conn.prepareStatement("UPDATE todos SET completed = TRUE WHERE id = ?");
                    pstmt.setInt(1, id);
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Todo markerad som klar!");
                    } else {
                        System.out.println("Ingen todo hittades med det ID:t.");
                    }
                    pstmt.close();
                } else if (choice == 4) {
                    System.out.print("Ange ID för todo att ta bort: ");
                    int id = scanner.nextInt();

                    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM todos WHERE id = ?");
                    pstmt.setInt(1, id);
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Todo borttagen!");
                    } else {
                        System.out.println("Ingen todo hittades med det ID:t.");
                    }
                    pstmt.close();
                } else if (choice == 5) {
                    running = false;
                    System.out.println("Avslutar programmet...");
                } else {
                    System.out.println("Ogiltigt val. Försök igen.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Fel vid stängning av databasanslutning: " + e.getMessage());
                }
            }
        }
    }
}
