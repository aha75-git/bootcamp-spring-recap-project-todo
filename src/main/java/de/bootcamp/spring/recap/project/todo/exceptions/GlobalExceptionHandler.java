package de.bootcamp.spring.recap.project.todo.exceptions;

import de.bootcamp.spring.recap.project.todo.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CardNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleCardNotFoundException(CardNotFoundException e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND.toString(), Arrays.toString(e.getStackTrace()), LocalDateTime.now());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNullPointerException(NullPointerException e) {
        return new ErrorMessage(e.getMessage(), HttpStatus.NOT_FOUND.toString(), Arrays.toString(e.getStackTrace()), LocalDateTime.now());
    }
}
