package se.dsve.service;

import se.dsve.dao.TodoDAO;
import se.dsve.entity.Todo;

import java.util.List;
import java.util.Optional;

public class TodoService {
    private final TodoDAO todoDAO;

    public TodoService(TodoDAO todoDAO) {
        this.todoDAO = todoDAO;
    }

    public void addTodo (Todo todo) {
        todoDAO.save(todo);
    }

    public Optional<Todo> getTodoById(int id){
        return todoDAO.findById(id);
    }

    public List<Todo> getAllTodo () {
        return todoDAO.findAll();
    }

    public void updateTodo (Todo todo) {
        todoDAO.update(todo);
    }

    public void deleteTodo (Todo todo) {
        todoDAO.delete(todo);
    }
}
