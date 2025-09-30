package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.TodoRequestDto;
import com.emobile.springtodo.dto.TodoResponseDto;
import com.emobile.springtodo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@Tag(name = "Todo Management", description = "API for managing TODO items")
public class TodoController {
    private final TodoService service;

    public TodoController(TodoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new TODO item", description = "Creates a new TODO item with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TODO item created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.emobile.springtodo.model.ErrorResponse.class)))
    })
    public TodoResponseDto create(@Valid @RequestBody TodoRequestDto dto) {
        return service.create(dto);
    }

    @GetMapping
    @Operation(summary = "Get all TODO items", description = "Retrieves a paginated list of all TODO items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TODO items retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponseDto.class)))
    })
    public List<TodoResponseDto> getAll(
            @Parameter(description = "Maximum number of items to return", example = "10")
            @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Number of items to skip", example = "0")
            @RequestParam(defaultValue = "0") int offset) {
        return service.findAll(limit, offset);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get TODO item by ID", description = "Retrieves a specific TODO item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TODO item found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "TODO item not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.emobile.springtodo.model.ErrorResponse.class)))
    })
    public TodoResponseDto getById(
            @Parameter(description = "ID of the TODO item to retrieve", example = "1")
            @PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update TODO item", description = "Updates an existing TODO item with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TODO item updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.emobile.springtodo.model.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "TODO item not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.emobile.springtodo.model.ErrorResponse.class)))
    })
    public TodoResponseDto update(
            @Parameter(description = "ID of the TODO item to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TodoRequestDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete TODO item", description = "Deletes a TODO item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TODO item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "TODO item not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.emobile.springtodo.model.ErrorResponse.class)))
    })
    public void delete(
            @Parameter(description = "ID of the TODO item to delete", example = "1")
            @PathVariable Long id) {
        service.delete(id);
    }
}
