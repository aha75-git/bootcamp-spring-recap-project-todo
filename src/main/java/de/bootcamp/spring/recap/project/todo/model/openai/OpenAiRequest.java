package de.bootcamp.spring.recap.project.todo.model.openai;

import java.util.List;

/**
 * {
 *     "model": "gpt-5",
 *     "messages": [
 *       {
 *         "role": "developer",
 *         "content": "You are a helpful assistant."
 *       }
 *     ]
 *   }
 */
public record OpenAiRequest(String model, List<OpenAiMessage> messages) {
}
