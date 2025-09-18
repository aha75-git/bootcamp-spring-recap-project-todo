package de.bootcamp.spring.recap.project.todo.exceptions;

public class CardNotFoundException extends Exception {
    public CardNotFoundException(String message) {
        super(message);
    }
}
