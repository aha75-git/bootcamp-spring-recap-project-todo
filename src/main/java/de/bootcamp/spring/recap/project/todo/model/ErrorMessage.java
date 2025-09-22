package de.bootcamp.spring.recap.project.todo.model;

import java.time.LocalDateTime;

public record ErrorMessage(String message, String status, String detail, LocalDateTime timestamp) {
}

