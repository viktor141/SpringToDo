package com.emobile.springtodo.repository;

import com.emobile.springtodo.SpringToDoApplicationTests;
import com.emobile.springtodo.model.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TodoRepository Integration Tests")
class TodoRepositoryIntegrationTest extends SpringToDoApplicationTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    @DisplayName("Should handle pagination correctly")
    @Sql(scripts = {"/db/cleanup.sql", "/db/insert_test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldHandlePaginationCorrectly() {
        // When - get first page
        List<Todo> firstPage = todoRepository.findAll(2, 0);
        
        // Then
        assertEquals(2, firstPage.size());
        assertEquals("Test TODO 1", firstPage.get(0).description());
        assertEquals("Test TODO 2", firstPage.get(1).description());

        // When - get second page
        List<Todo> secondPage = todoRepository.findAll(2, 2);
        
        // Then
        assertEquals(1, secondPage.size());
        assertEquals("Test TODO 3", secondPage.get(0).description());
    }

    @Test
    @DisplayName("Should return null when finding non-existent TODO by ID")
    @Sql(scripts = "/db/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void shouldReturnNullWhenFindingNonExistentTodoById() {
        // When
        Todo todo = todoRepository.findById(999L);

        // Then
        assertNull(todo);
    }
}
