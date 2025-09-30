package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.TodoRequestDto;
import com.emobile.springtodo.dto.TodoResponseDto;
import com.emobile.springtodo.exception.TodoNotFoundException;
import com.emobile.springtodo.mapper.TodoMapper;
import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.repository.TodoRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final TodoRepository repository;
    private final TodoMapper todoMapper;

    public TodoService(TodoRepository repository, TodoMapper todoMapper) {
        this.repository = repository;
        this.todoMapper = todoMapper;
    }

    public TodoResponseDto create(TodoRequestDto dto) {
        Todo todo = todoMapper.toEntity(dto);
        Todo savedTodo = repository.save(todo);
        return todoMapper.toDto(savedTodo);
    }

    @Cacheable("todos")
    public List<TodoResponseDto> findAll(int limit, int offset) {
        return repository.findAll(limit, offset).stream()
                .map(todoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable("todo")
    public TodoResponseDto findById(Long id) {
        Todo todo = repository.findById(id);
        if (todo == null) throw new TodoNotFoundException(id);
        return todoMapper.toDto(todo);
    }

    @CacheEvict(value = {"todos", "todo"}, allEntries = true)
    public TodoResponseDto update(Long id, TodoRequestDto dto) {
        Todo existing = repository.findById(id);
        if (existing == null) throw new TodoNotFoundException(id);
        Todo updated = new Todo(id, dto.description(), dto.status(), existing.createdAt(), new Timestamp(System.currentTimeMillis()));
        Todo savedTodo = repository.save(updated);
        return todoMapper.toDto(savedTodo);
    }

    @CacheEvict(value = {"todos", "todo"}, allEntries = true)
    public void delete(Long id) {
        Todo todo = repository.findById(id);
        if (todo == null) throw new TodoNotFoundException(id);
        repository.deleteById(id);
    }
}
