package de.bootcamp.spring.recap.project.todo.repository;

import de.bootcamp.spring.recap.project.todo.model.Card;
import de.bootcamp.spring.recap.project.todo.model.CardStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {
    List<Card> findCardsByStatus(CardStatus status);
}
