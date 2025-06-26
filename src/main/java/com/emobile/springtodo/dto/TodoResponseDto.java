package com.emobile.springtodo.dto;

public record TodoResponseDto(Long id, String description, String status, String createdAt, String updatedAt) {}
