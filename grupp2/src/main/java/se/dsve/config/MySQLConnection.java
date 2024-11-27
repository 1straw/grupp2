package se.dsve.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import se.dsve.entity.Todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection implements DatabaseConnection {
    private static SessionFactory sessionFactory;

    @Override
    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                /* Attempt to create the database if it doesn't exist */
                createDatabaseIfNotExists();

                Configuration configuration = new Configuration();
                configuration.setProperty("hibernate.connection.url", DatabaseConfig.getDbUrl());
                configuration.setProperty("hibernate.connection.username", DatabaseConfig.getDbUsername());
                configuration.setProperty("hibernate.connection.password", DatabaseConfig.getDbPassword());
                configuration.setProperty("hibernate.dialect", DatabaseConfig.getDbDialect());
                configuration.setProperty("hibernate.hbm2ddl.auto", "update");
                configuration.setProperty("hibernate.show_sql", "false");

                configuration.addAnnotatedClass(Todo.class);

                sessionFactory = configuration.buildSessionFactory();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sessionFactory;
    }

    private void createDatabaseIfNotExists() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "to_do";
        String username = DatabaseConfig.getDbUsername();
        String password = DatabaseConfig.getDbPassword();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            // Check if database exists
            String checkDbQuery = "SHOW DATABASES LIKE '" + dbName + "'";
            var resultSet = statement.executeQuery(checkDbQuery);

            if (!resultSet.next()) {
                // Database don't exist!
                String creatDbQuery = "CREATE DATABASE " + dbName;
                statement.execute(creatDbQuery);
                System.out.println("Database " + dbName + " created successfully");
            } else {
                System.out.println("Database " + dbName + " already exists");
            }
        }
    }
}