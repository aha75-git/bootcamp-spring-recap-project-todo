package de.bootcamp.spring.recap.project.todo.service;

import de.bootcamp.spring.recap.project.todo.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardDoingService {
    private CardRepository cardRepository;
}
