package com.emobile.springtodo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for TODO item")
public record TodoResponseDto(
        @Schema(description = "Unique identifier of the TODO item", example = "1")
        Long id,
        
        @Schema(description = "Description of the TODO item", example = "Complete project documentation")
        String description,
        
        @Schema(description = "Status of the TODO item", example = "TODO")
        String status,
        
        @Schema(description = "Creation timestamp", example = "2024-01-01 10:00:00")
        String createdAt,
        
        @Schema(description = "Last update timestamp", example = "2024-01-01 12:00:00")
        String updatedAt
) {}
