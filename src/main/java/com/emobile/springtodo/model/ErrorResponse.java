package com.emobile.springtodo.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error response DTO")
public record ErrorResponse(
        @Schema(description = "Error message", example = "Todo with id 1 not found")
        String message
) {
}
