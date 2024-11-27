package se.dsve.config;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConfig {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getDbUrl() {
        return dotenv.get("DB_URL");
    }

    public static String getDbUsername() {
        return dotenv.get("DB_USERNAME");
    }

    public static String getDbPassword() {
        return dotenv.get("DB_PASSWORD");
    }

    public static String getDbDialect() {
        return dotenv.get("DB_DIALECT");
    }
}
