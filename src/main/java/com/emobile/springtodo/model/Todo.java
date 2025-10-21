package com.emobile.springtodo.model;

import java.sql.Timestamp;

public record Todo(Long id, String description, Status status, Timestamp createdAt, Timestamp updatedAt) {
    public enum Status {TODO, IN_PROGRESS, DONE}
}
