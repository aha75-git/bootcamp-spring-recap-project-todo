package de.bootcamp.spring.recap.project.todo.service;

import de.bootcamp.spring.recap.project.todo.model.Card;
import de.bootcamp.spring.recap.project.todo.model.CardStatus;
import de.bootcamp.spring.recap.project.todo.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CardDoingService {
    private final CardRepository cardRepository;
    private final IdService idService;

    public List<Card> getAllCards() {
        return this.cardRepository.findCardsByStatus(CardStatus.IN_PROGRESS);
    }

}
