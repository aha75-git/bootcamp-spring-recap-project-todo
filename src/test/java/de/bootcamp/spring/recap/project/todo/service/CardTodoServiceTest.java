package de.bootcamp.spring.recap.project.todo.service;

import de.bootcamp.spring.recap.project.todo.dto.CardDto;
import de.bootcamp.spring.recap.project.todo.exceptions.CardNotFoundException;
import de.bootcamp.spring.recap.project.todo.model.Card;
import de.bootcamp.spring.recap.project.todo.model.CardStatus;
import de.bootcamp.spring.recap.project.todo.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardTodoServiceTest {
    CardDto cardDto;
    Card card1;
    Card card2;
    List<Card> cards;
    IdService mockIdService;
    CardRepository mockRepo;
    CardTodoService cardTodoService;

    @BeforeEach
    void setUp() {
        cardDto = new CardDto("Ticket 1 ist in TODO", CardStatus.OPEN);
        card1 = new Card("1", "Ticket 1 ist in TODO", CardStatus.OPEN);
        card2 = new Card("2", "Ticket 2 ist in TODO", CardStatus.OPEN);
        cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        mockIdService = mock(IdService.class);
        mockRepo = mock(CardRepository.class);
        cardTodoService = new CardTodoService(mockRepo, mockIdService);
    }

    @Test
    void getAllCards_shouldReturnCards_whenStatusIsOpen() {
        // GIVEN
        when(mockRepo.findAll()).thenReturn(cards);

        // WHEN
        var actual = cardTodoService.getAllCards();

        // THEN
        verify(mockRepo).findAll();
        assertEquals(cards.size(), actual.size());
    }

    @Test
    void addCard_shouldAddCard_whenCalledWithStatusIsOpen() {
        // GIVEN
        when(mockRepo.save(any(Card.class))).thenReturn(card1);
        when(mockIdService.randomId()).thenReturn(card1.id());

        // WHEN
        Card actual = cardTodoService.addCard(cardDto);

        // THEN
        verify(mockIdService).randomId();
        verify(mockRepo).save(any(Card.class));
        assertEquals(card1, actual);
    }

    @Test
    void getCardById_shouldReturnCard_whenCalledWithValidId() {
        // GIVEN
        when(mockRepo.findById(card1.id())).thenReturn(Optional.of(card1));

        // WHEN
        var actual = cardTodoService.getCardById(card1.id());

        // THEN
        verify(mockRepo).findById(card1.id());
        assertEquals(card1, actual);
    }

    @Test
    void getCardById_shouldReturnNull_whenCalledWithInvalidId() {
        // GIVEN
        String id = "5";
        when(mockRepo.findById(id)).thenReturn(Optional.empty());

        // WHEN
        var actual = cardTodoService.getCardById(id);

        // THEN
        verify(mockRepo).findById(id);
        assertNull(actual);
    }

    @Test
    void updateCard_shouldUpdateCard_whenCalledWithValidDataAndId() {
        // GIVEN
        String id = "1";
        Card cardExist = new Card(id, "Ticket Test TODO", CardStatus.OPEN);
        when(mockIdService.randomId()).thenReturn(id);
        when(mockRepo.findById(id)).thenReturn(Optional.of(cardExist));
        when(mockRepo.save(any(Card.class))).thenReturn(card1);

        // WHEN
        var actual = cardTodoService.updateCard(id, cardDto);

        // THEN
        verify(mockRepo).findById(id);
        verify(mockRepo).save(any(Card.class));
        assertEquals(card1, actual);
    }

    @Test
    void updateCard_shouldNotUpdateCard_whenCalledWithValidDataAndInvalidId() {
        // GIVEN
        String id = "5";
        when(mockIdService.randomId()).thenReturn(id);
        when(mockRepo.findById(id)).thenReturn(Optional.empty());

        // WHEN
        var actual = cardTodoService.updateCard(id, cardDto);

        // THEN
        verify(mockRepo).findById(id);
        assertNotEquals(card1, actual);
        assertNull(actual);
    }

    @Test
    void deleteCardById_shouldDeleteCard_whenCalledWithValidId() throws CardNotFoundException {
        // GIVEN
        when(mockRepo.findById(card1.id())).thenReturn(Optional.of(card1));

        // WHEN
        cardTodoService.deleteCardById(card1.id());

        // THEN
        verify(mockRepo).deleteById(card1.id());
    }

    @Test
    void deleteCardById_shouldDeleteCard_whenCalledWithInvalidId() {
        // GIVEN
        String id = "5";
        when(mockRepo.findById(id)).thenReturn(Optional.empty());

        // WHEN
        // THEN
        assertThrows(CardNotFoundException.class, () -> cardTodoService.deleteCardById(id));
    }
}