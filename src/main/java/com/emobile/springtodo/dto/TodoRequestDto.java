package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.Todo;

public record TodoRequestDto(String description, Todo.Status status) {}
