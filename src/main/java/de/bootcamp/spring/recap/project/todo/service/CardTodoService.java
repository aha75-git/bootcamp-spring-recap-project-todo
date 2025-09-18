package de.bootcamp.spring.recap.project.todo.service;

import de.bootcamp.spring.recap.project.todo.dto.CardDto;
import de.bootcamp.spring.recap.project.todo.exceptions.CardNotFoundException;
import de.bootcamp.spring.recap.project.todo.model.Card;
import de.bootcamp.spring.recap.project.todo.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Stack;

@Service
public class CardTodoService {
    private final Stack<Card> undoStack = new Stack<>();
    private final Stack<Card> redoStack = new Stack<>();
    private final CardRepository cardRepository;
    private final IdService idService;

    public CardTodoService(CardRepository cardRepository, IdService idService) {
        this.cardRepository = cardRepository;
        this.idService = idService;
    }

    public List<Card> getAllCards() {
        //return this.cardRepository.findCardsByStatus(CardStatus.OPEN);
        return this.cardRepository.findAll();
    }

    public Card addCard(CardDto cardDto) {
        Card card = this.cardRepository.save(
                new Card(idService.randomId(),
                        cardDto.description(),
                        cardDto.status())
        );
        undoStack.push(card); // Für Undo-Funktionalität
        return card;
    }

    public Card getCardById(String id) {
        return this.cardRepository.findById(id).orElse(null);
    }

    public Card updateCard(String id, CardDto cardDto) {
        Card card = null;
        Card cardOld = this.cardRepository.findById(id).orElse(null);
        if (cardOld != null) {
            card = this.cardRepository.save(cardOld
                    .withDescription(cardDto.description())
                    .withStatus(cardDto.status())
            );
            undoStack.push(card); // Für Undo-Funktionalität
        }
        return card;
    }

    public void deleteCardById(String id) throws CardNotFoundException {
        Card card = this.cardRepository.findById(id).orElse(null);
        if (card == null) {
            throw new CardNotFoundException("Das Ticket mit der ID: " + id + " existiert nicht!");
        }
        undoStack.push(card); // Für Undo-Funktionalität
        this.cardRepository.deleteById(id);
    }

    // TODO
    /* */
    public Card undoLastChange() {
        if (!undoStack.isEmpty()) {
            Card lastTodo = undoStack.pop();
            //todoHistoryRepository.save(lastTodo); // Speichern in der Historie
            //todoRepository.delete(lastTodo); // Löschen des letzten Todos
            redoStack.push(lastTodo); // Für Redo-Funktionalität
            return lastTodo;
        }
        return null;
    }

    public Card redoLastUndo() {
        if (!redoStack.isEmpty()) {
            Card lastUndoneTodo = redoStack.pop();
            //todoRepository.save(lastUndoneTodo); // Wiederherstellen des Todos
            undoStack.push(lastUndoneTodo); // Für Undo-Funktionalität
            return lastUndoneTodo;
        }
        return null;
    }


}
