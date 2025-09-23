package de.bootcamp.spring.recap.project.todo.model.openai;

/**
 *   {
 *        "role": "developer",
 *        "content": "You are a helpful assistant."
 *    }
 */
public record OpenAiMessage(String role, String content) {
}
