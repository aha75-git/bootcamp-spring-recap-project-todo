package de.bootcamp.spring.recap.project.todo.service;

import de.bootcamp.spring.recap.project.todo.model.openai.OpenAiMessage;
import de.bootcamp.spring.recap.project.todo.model.openai.OpenAiRequest;
import de.bootcamp.spring.recap.project.todo.model.openai.OpenAiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;

@Service
public class ChatGPTService {

    private final RestClient restClient;

    public ChatGPTService(RestClient.Builder restClientBuilder, @Value("${API_KEY}") String apiKey) {
        this.restClient = restClientBuilder
                .baseUrl("https://api.openai.com/v1/chat/completions")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public String createChatCompletionAndCheckSpelling(String message) {
        //String requestBody = "{ \"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"Please check the spelling of the following text: " + text + "\"}] }";

        System.out.println("Message für OPEN-AI: " + message);

        OpenAiMessage openAiMessage = new OpenAiMessage("user", message);
        OpenAiRequest openAiRequest = new OpenAiRequest("gpt-5", Collections.singletonList(openAiMessage));
        return restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(openAiRequest)
                .retrieve()
                .body(OpenAiResponse.class).choices().get(0).message().content();
    }
}
