package de.bootcamp.spring.recap.project.todo.dto;

import de.bootcamp.spring.recap.project.todo.model.CardStatus;
import lombok.With;

@With
public record CardDto(String description, CardStatus status) {
}
