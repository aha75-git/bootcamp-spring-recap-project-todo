package de.bootcamp.spring.recap.project.todo.model.openai;

/**
 *    {
 *        "message": {
 *          "role": "assistant",
 *          "content": "Hello! How can I assist you today?",
 *        }
 *     }
 */
public record OpenAiChoice(OpenAiMessage message) {
}
