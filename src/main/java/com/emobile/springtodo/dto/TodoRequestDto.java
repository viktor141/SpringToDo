package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request DTO for creating or updating a TODO item")
public record TodoRequestDto(
        @Schema(description = "Description of the TODO item", example = "Complete project documentation", required = true)
        @NotBlank(message = "Description cannot be blank")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,
        
        @Schema(description = "Status of the TODO item", example = "TODO", required = true)
        @NotNull(message = "Status cannot be null")
        Todo.Status status
) {}
