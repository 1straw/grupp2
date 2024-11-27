package se.dsve.config;

import org.hibernate.SessionFactory;

public interface DatabaseConnection {
    SessionFactory getSessionFactory();
}
