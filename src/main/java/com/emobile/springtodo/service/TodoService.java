package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.TodoRequestDto;
import com.emobile.springtodo.dto.TodoResponseDto;
import com.emobile.springtodo.exception.TodoNotFoundException;
import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.repository.TodoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final TodoRepository repository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public TodoService(TodoRepository repository) {
        this.repository = repository;
    }

    public TodoResponseDto create(TodoRequestDto dto) {
        Todo todo = toEntity(dto);
        Todo savedTodo = repository.save(todo);
        return toDto(savedTodo);
    }

    @Cacheable("todos")
    public List<TodoResponseDto> findAll(int limit, int offset) {
        return repository.findAll(limit, offset).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable("todo")
    public TodoResponseDto findById(Long id) {
        Todo todo = repository.findById(id);
        if (todo == null) throw new TodoNotFoundException(id);
        return toDto(todo);
    }

    @CacheEvict(value = {"todos", "todo"}, allEntries = true)
    public TodoResponseDto update(Long id, TodoRequestDto dto) {
        Todo existing = repository.findById(id);
        if (existing == null) throw new TodoNotFoundException(id);
        Todo updated = new Todo(id, dto.description(), dto.status(), existing.createdAt(), new Timestamp(System.currentTimeMillis()));
        Todo savedTodo = repository.save(updated);
        return toDto(savedTodo);
    }

    @CacheEvict(value = {"todos", "todo"}, allEntries = true)
    public void delete(Long id) {
        Todo todo = repository.findById(id);
        if (todo == null) throw new TodoNotFoundException(id);
        repository.deleteById(id);
    }

    private Todo toEntity(TodoRequestDto dto) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return new Todo(null, dto.description(), dto.status(), now, now);
    }

    private TodoResponseDto toDto(Todo todo) {
        String createdAt = dateFormat.format(todo.createdAt());
        String updatedAt = dateFormat.format(todo.updatedAt());
        return new TodoResponseDto(todo.id(), todo.description(), todo.status().name(), createdAt, updatedAt);
    }
}
