package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.TodoRequestDto;
import com.emobile.springtodo.exception.TodoNotFoundException;
import com.emobile.springtodo.mapper.TodoMapper;
import com.emobile.springtodo.model.Todo;
import com.emobile.springtodo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoService Error Handling Tests")
class TodoServiceErrorHandlingTest {

    @Mock
    private TodoRepository repository;

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoService todoService;

    private TodoRequestDto testRequestDto;

    @BeforeEach
    void setUp() {
        testRequestDto = new TodoRequestDto("Test task", Todo.Status.TODO);
    }

    @Test
    @DisplayName("Should throw TodoNotFoundException when finding non-existent todo by ID")
    void shouldThrowTodoNotFoundExceptionWhenFindingNonExistentTodoById() {
        // Given
        Long nonExistentId = 999L;
        when(repository.findById(nonExistentId)).thenReturn(null);

        // When & Then
        TodoNotFoundException exception = assertThrows(TodoNotFoundException.class, 
                () -> todoService.findById(nonExistentId));
        
        assertEquals("Todo with id 999 not found", exception.getMessage());
        verify(repository).findById(nonExistentId);
        verify(todoMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should throw TodoNotFoundException when updating non-existent todo")
    void shouldThrowTodoNotFoundExceptionWhenUpdatingNonExistentTodo() {
        // Given
        Long nonExistentId = 999L;
        when(repository.findById(nonExistentId)).thenReturn(null);

        // When & Then
        TodoNotFoundException exception = assertThrows(TodoNotFoundException.class, 
                () -> todoService.update(nonExistentId, testRequestDto));
        
        assertEquals("Todo with id 999 not found", exception.getMessage());
        verify(repository).findById(nonExistentId);
        verify(repository, never()).save(any());
        verify(todoMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should throw TodoNotFoundException when deleting non-existent todo")
    void shouldThrowTodoNotFoundExceptionWhenDeletingNonExistentTodo() {
        // Given
        Long nonExistentId = 999L;
        when(repository.findById(nonExistentId)).thenReturn(null);

        // When & Then
        TodoNotFoundException exception = assertThrows(TodoNotFoundException.class, 
                () -> todoService.delete(nonExistentId));
        
        assertEquals("Todo with id 999 not found", exception.getMessage());
        verify(repository).findById(nonExistentId);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should propagate RuntimeException when repository throws exception")
    void shouldPropagateRuntimeExceptionWhenRepositoryThrowsException() {
        // Given
        Long testId = 1L;
        when(repository.findById(testId)).thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> todoService.findById(testId));
        
        assertEquals("Database connection failed", exception.getMessage());
        verify(repository).findById(testId);
    }

    @Test
    @DisplayName("Should propagate RuntimeException when repository save fails")
    void shouldPropagateRuntimeExceptionWhenRepositorySaveFails() {
        // Given
        doThrow(new RuntimeException("Database save failed")).when(repository).save(any());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> todoService.create(testRequestDto));
        
        assertEquals("Database save failed", exception.getMessage());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Should propagate DataIntegrityViolationException when NOT NULL constraint is violated")
    void shouldPropagateDataIntegrityViolationExceptionWhenNotNullConstraintViolated() {
        // Given
        doThrow(new DataIntegrityViolationException("NOT NULL constraint failed: todos.description"))
                .when(repository).save(any());

        // When & Then
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, 
                () -> todoService.create(testRequestDto));
        
        assertEquals("NOT NULL constraint failed: todos.description", exception.getMessage());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Should propagate DataIntegrityViolationException when VARCHAR length constraint is violated")
    void shouldPropagateDataIntegrityViolationExceptionWhenVarcharLengthConstraintViolated() {
        // Given
        doThrow(new DataIntegrityViolationException("VARCHAR value too long for column description"))
                .when(repository).save(any());

        // When & Then
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, 
                () -> todoService.create(testRequestDto));
        
        assertEquals("VARCHAR value too long for column description", exception.getMessage());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Should propagate DataIntegrityViolationException when PRIMARY KEY constraint is violated")
    void shouldPropagateDataIntegrityViolationExceptionWhenPrimaryKeyConstraintViolated() {
        // Given
        doThrow(new DataIntegrityViolationException("PRIMARY KEY constraint failed: todos.id"))
                .when(repository).save(any());

        // When & Then
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, 
                () -> todoService.create(testRequestDto));
        
        assertEquals("PRIMARY KEY constraint failed: todos.id", exception.getMessage());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Should propagate DataIntegrityViolationException when updating with constraint violation")
    void shouldPropagateDataIntegrityViolationExceptionWhenUpdatingWithConstraintViolation() {
        // Given
        Long testId = 1L;
        Todo existingTodo = new Todo(testId, "Existing task", Todo.Status.TODO, 
                new java.sql.Timestamp(System.currentTimeMillis()), 
                new java.sql.Timestamp(System.currentTimeMillis()));
        
        when(repository.findById(testId)).thenReturn(existingTodo);
        doThrow(new DataIntegrityViolationException("NOT NULL constraint failed: todos.status"))
                .when(repository).save(any());

        // When & Then
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, 
                () -> todoService.update(testId, testRequestDto));
        
        assertEquals("NOT NULL constraint failed: todos.status", exception.getMessage());
        verify(repository).findById(testId);
        verify(repository).save(any());
    }

}
