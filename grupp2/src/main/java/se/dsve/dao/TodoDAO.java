package se.dsve.dao;
import se.dsve.entity.Todo;
import java.util.List;
import java.util.Optional;

public interface TodoDAO {
    void save(Todo todo);
    Optional<Todo> findById(int id);
    List<Todo> findAll();
    void update(Todo todo);
    void delete(Todo todo);
}
