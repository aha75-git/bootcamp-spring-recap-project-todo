package de.bootcamp.spring.recap.project.todo.service;

import de.bootcamp.spring.recap.project.todo.model.Card;
import de.bootcamp.spring.recap.project.todo.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@AllArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    @GetMapping
    public List<Card> findAll() {
        return cardRepository.findAll();
    }
}
