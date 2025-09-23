package de.bootcamp.spring.recap.project.todo.controller;

import de.bootcamp.spring.recap.project.todo.model.Card;
import de.bootcamp.spring.recap.project.todo.model.CardStatus;
import de.bootcamp.spring.recap.project.todo.model.openai.OpenAiResponse;
import de.bootcamp.spring.recap.project.todo.repository.CardRepository;
import de.bootcamp.spring.recap.project.todo.service.ChatGPTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestClient;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@SpringBootTest
@AutoConfigureMockMvc
public class CardTodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardRepository repo;

    private MockRestServiceServer mockServer;

    @Autowired
    RestClient.Builder restClientBuilder;

    @Autowired
    private ChatGPTService  chatGPTService;

    @Value("${API_KEY}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        repo.save(new Card("1", "Ticket 1 ist in TODO", CardStatus.OPEN));
        repo.save(new Card("2", "Frontend-Setup mit HTML", CardStatus.OPEN));
        mockServer = MockRestServiceServer.bindTo(restClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions")).build();
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
                                   "description": "Frontend-Setup mit HTML",
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
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Das Ticket mit der ID: " + id + " existiert nicht!"));;
    }

    @Test
    void addCard_shouldAddAndReturnCard_whenCalled() throws Exception {
        // GIVEN

        /* */
        System.out.println(apiKey);

        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer " + apiKey))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess("""
                        {
                              "choices": [
                                {
                                   "message": {
                                    "role": "user",
                                    "content": "Frontend-Setup mit HTML",
                                  }
                                }
                              ]
                        }
                        """, MediaType.APPLICATION_JSON));

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "description": "Frontend-Setup mit HTML",
                                   "status": "OPEN"
                                 }
                                """))
                // THEN
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                  {
                                     "description": "Frontend-Setup mit HTML",
                                     "status": "OPEN"
                                 }
                                  """
                ))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Frontend-Setup mit HTML"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OPEN"));
    }

    @Test
    void updateCard_shouldUpdateAndReturnCard_whenCalled() throws Exception {
        // GIVEN
        String id = "1";

        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer " + apiKey))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess("""
                        {
                              "choices": [
                                {
                                   "message": {
                                    "role": "user",
                                    "content": "Ticket 1 ist aktualisiert.",
                                  }
                                }
                              ]
                        }
                        """, MediaType.APPLICATION_JSON));

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
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Das Ticket mit der ID: " + id + " existiert nicht!"));;
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/" + id))
        // THEN
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Das Ticket mit der ID: " + id + " existiert nicht!"));
        ;

    }
}