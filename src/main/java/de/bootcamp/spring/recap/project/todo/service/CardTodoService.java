package de.bootcamp.spring.recap.project.todo.service;

import de.bootcamp.spring.recap.project.todo.dto.CardDto;
import de.bootcamp.spring.recap.project.todo.exceptions.CardNotFoundException;
import de.bootcamp.spring.recap.project.todo.model.Card;
import de.bootcamp.spring.recap.project.todo.model.CardStatus;
import de.bootcamp.spring.recap.project.todo.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CardTodoService {

    private final CardRepository cardRepository;
    private final IdService idService;

    public List<Card> getAllCards() {
        return this.cardRepository.findCardsByStatus(CardStatus.OPEN);
    }

    public Card addCard(CardDto cardDto) {
        return this.cardRepository.save(
                new Card(idService.randomId(),
                        cardDto.description(),
                        cardDto.status())
        );
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
        }
        return card;
    }

    public void deleteCardById(String id) throws CardNotFoundException {
        Card card = this.cardRepository.findById(id).orElse(null);
        if (card == null) {
            throw new CardNotFoundException("Das Ticket mit der ID: " + id + " existiert nicht!");
        }
        this.cardRepository.deleteById(id);
    }
}
