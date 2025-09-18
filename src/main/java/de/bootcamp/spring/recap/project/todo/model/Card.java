package de.bootcamp.spring.recap.project.todo.model;

import lombok.With;

@With
public record Card(String id, String description, CardStatus status) {
}
