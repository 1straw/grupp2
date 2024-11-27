package se.dsve.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "todo")
@Data
@NoArgsConstructor

public class Todo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private boolean completed = false;

    public Todo(String description) {
        this.description = description;

    }
}
