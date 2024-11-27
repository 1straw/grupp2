package se.dsve;

import se.dsve.config.DatabaseConnection;
import se.dsve.config.MySQLConnection;
import se.dsve.dao.TodoDAO;
import se.dsve.dao.impl.TodoDAOImpl;
import se.dsve.entity.Todo;
import se.dsve.service.TodoService;
import se.dsve.ui.MenuHandler;

public class Test {
    public static void main(String[] args) {

        // init db conn
        DatabaseConnection dbConnection = new MySQLConnection();

        // init DAOs
        TodoDAO todoDAO = new TodoDAOImpl(dbConnection.getSessionFactory());

        // init Services
        TodoService todoService = new TodoService(todoDAO);

        // init and start menu
        MenuHandler menuHandler = new MenuHandler(todoService);

        //menuHandler.chooseDatabase();
        menuHandler.displayMenu();
    }
}