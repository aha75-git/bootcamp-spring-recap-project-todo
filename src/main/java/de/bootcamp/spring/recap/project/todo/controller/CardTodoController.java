package de.bootcamp.spring.recap.project.todo.controller;

import de.bootcamp.spring.recap.project.todo.dto.CardDto;
import de.bootcamp.spring.recap.project.todo.exceptions.CardNotFoundException;
import de.bootcamp.spring.recap.project.todo.model.Card;
import de.bootcamp.spring.recap.project.todo.service.CardTodoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo")
@AllArgsConstructor
public class CardTodoController {

    private final CardTodoService cardTodoService;

    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        return new ResponseEntity<>(this.cardTodoService.getAllCards(),  HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable String id) throws CardNotFoundException {
        Card card = this.cardTodoService.getCardById(id);
        if (card == null) {
            throw new CardNotFoundException("Das Ticket mit der ID: " + id + " existiert nicht!");
        }
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Card> addCard(@RequestBody CardDto cardDto) {
        Card createdCard = this.cardTodoService.addCard(cardDto);
        return new ResponseEntity<>(createdCard,  HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(@PathVariable String id, @RequestBody CardDto cardDto) throws CardNotFoundException {
        Card updatedCard = this.cardTodoService.updateCard(id, cardDto);
        if (updatedCard == null) {
            throw new CardNotFoundException("Das Ticket mit der ID: " + id + " existiert nicht!");
        }
        return new ResponseEntity<>(updatedCard,  HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardById(@PathVariable String id) throws CardNotFoundException {
        this.cardTodoService.deleteCardById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/undo")
    public ResponseEntity<Card> undoLastChange() {
        Card undoCard = this.cardTodoService.undoLastChange();
        if (undoCard == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(undoCard,  HttpStatus.OK);
    }

    @GetMapping("/redo")
    public ResponseEntity<Card> redoLastUndo() {
        Card redoCard = this.cardTodoService.redoLastUndo();
        if (redoCard == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(redoCard,  HttpStatus.OK);
    }
}
