package de.bootcamp.spring.recap.project.todo.controller;

import de.bootcamp.spring.recap.project.todo.model.Card;
import de.bootcamp.spring.recap.project.todo.model.CardStatus;
import de.bootcamp.spring.recap.project.todo.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class CardTodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardRepository repo;

    @BeforeEach
    void setUp() {
        repo.save(new Card("1", "Ticket 1 ist in TODO", CardStatus.OPEN));
        repo.save(new Card("2", "Ticket 2 ist in TODO", CardStatus.OPEN));
    }

    @Test
    void getAllCards_shouldReturnListOfCards_whenCalled() throws Exception {
        // GIVEN
        if(repo.findAll().size() == 3) {
            repo.deleteById("2");
        }

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
        // THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                        [
                              {
                                   "description": "Ticket 1 ist in TODO",
                                   "status": "OPEN"
                               },
                               {
                                   "description": "Ticket 2 ist in TODO",
                                   "status": "OPEN"
                               }
                        ]
                        """
                ));
    }

    @Test
    void getCardById_shouldReturnCard_whenCalledById() throws Exception {
        // GIVEN
        String id = "1";

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/" + id))
        // THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                        
                              {
                                  "id": "1",
                                   "description": "Ticket 1 ist in TODO",
                                   "status": "OPEN"
                               }
                        
                        """
                ));
    }

    @Test
    void getCardById_shouldReturnNotFound_whenCalledByInvalidId() throws Exception {
        // GIVEN
        String id = "5";

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/" + id))
        // THEN
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void addCard_shouldAddAndReturnCard_whenCalled() throws Exception {
        // GIVEN

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {                               
                                   "description": "Ticket 2 ist in TODO",
                                   "status": "OPEN"
                                 }                      
                                """))
                // THEN
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                  {
                                     "description": "Ticket 2 ist in TODO",
                                     "status": "OPEN"
                                 }
                                  """
                ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Ticket 2 ist in TODO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OPEN"));
    }

    @Test
    void updateCard_shouldUpdateAndReturnCard_whenCalled() throws Exception {
        // GIVEN
        String id = "1";

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {                               
                                     "description": "Ticket 1 ist aktualisiert.",
                                     "status": "OPEN"
                                 }                      
                                """))
                // THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                  {
                                     "description": "Ticket 1 ist aktualisiert.",
                                     "status": "OPEN"
                                 }
                                  """
                ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Ticket 1 ist aktualisiert."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OPEN"));
    }

    @Test
    void updateCard_shouldReturnNotFound_whenCalledWithInvalidId() throws Exception {
        // GIVEN
        String id = "5";

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {                               
                                     "description": "Ticket 1 ist aktualisiert.",
                                     "status": "OPEN"
                                 }                      
                                """))
                // THEN
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteCardById_shouldDeleteCard_whenCalled() throws Exception {
        // GIVEN
        String id = "1";

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/" + id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteCardById_shouldReturnNotFound_whenCalledWithInvalidId() throws Exception {
        // GIVEN
        String id = "5";

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}