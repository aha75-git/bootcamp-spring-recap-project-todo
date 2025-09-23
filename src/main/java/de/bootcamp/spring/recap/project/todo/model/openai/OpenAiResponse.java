package de.bootcamp.spring.recap.project.todo.model.openai;

import java.util.List;

/**
 * {
 *   "choices": [
 *     {
 *       "index": 0,
 *       "message": {
 *         "role": "assistant",
 *         "content": "Hello! How can I assist you today?",
 *       }
 *     }
 *   ]
 * }
 */
public record OpenAiResponse(List<OpenAiChoice> choices) {
}
