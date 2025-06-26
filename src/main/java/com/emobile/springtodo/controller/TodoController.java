package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.TodoRequestDto;
import com.emobile.springtodo.dto.TodoResponseDto;
import com.emobile.springtodo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @PostMapping
    public TodoResponseDto create(@Valid @RequestBody TodoRequestDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<TodoResponseDto> getAll(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int offset) {
        return service.findAll(limit, offset);
    }

    @GetMapping("/{id}")
    public TodoResponseDto getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public TodoResponseDto update(@PathVariable Long id, @Valid @RequestBody TodoRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
