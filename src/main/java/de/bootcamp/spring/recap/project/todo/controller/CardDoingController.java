package de.bootcamp.spring.recap.project.todo.controller;

import de.bootcamp.spring.recap.project.todo.service.CardDoingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doing")
@AllArgsConstructor
public class CardDoingController {
    private final CardDoingService cardDoingService;
}
