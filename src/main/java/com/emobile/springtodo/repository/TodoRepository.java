package com.emobile.springtodo.repository;


import com.emobile.springtodo.model.Todo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoRepository {
    private final JdbcTemplate jdbcTemplate;

    public TodoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Todo> findAll(int limit, int offset) {
        return jdbcTemplate.query(
                "SELECT * FROM todos ORDER BY id LIMIT ? OFFSET ?",
                new Object[]{limit, offset},
                (rs, rowNum) -> new Todo(
                        rs.getLong("id"),
                        rs.getString("description"),
                        Todo.Status.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                )
        );
    }

    public Todo findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM todos WHERE id = ?",
                new Object[]{id},
                (rs, rowNum) -> new Todo(
                        rs.getLong("id"),
                        rs.getString("description"),
                        Todo.Status.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                )
        );
    }

    public Todo save(Todo todo) {
        if (todo.id() == null) {
            jdbcTemplate.update(
                    "INSERT INTO todos (description, status, created_at, updated_at) VALUES (?, ?, ?, ?)",
                    todo.description(), todo.status().name(), todo.createdAt(), todo.updatedAt()
            );
            Long id = jdbcTemplate.queryForObject("SELECT LASTVAL()", Long.class);
            return new Todo(id, todo.description(), todo.status(), todo.createdAt(), todo.updatedAt());
        } else {
            jdbcTemplate.update(
                    "UPDATE todos SET description = ?, status = ?, updated_at = ? WHERE id = ?",
                    todo.description(), todo.status().name(), todo.updatedAt(), todo.id()
            );
            return todo;
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM todos WHERE id = ?", id);
    }
}
