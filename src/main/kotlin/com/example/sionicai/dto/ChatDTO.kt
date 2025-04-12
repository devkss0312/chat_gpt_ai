package com.example.sionicai.dto

import com.example.sionicai.entity.Chat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime

data class ChatDto(
        val id: Long,
        val question: String,
        val answer: String,
        val createdAt: LocalDateTime
) {
    companion object {
        fun from(chat: Chat): ChatDto {
            return ChatDto(
                    id = chat.id,
                    question = chat.question,
                    answer = chat.answer,
                    createdAt = chat.createdAt
            )
        }
    }
}
data class ChatRequest(
        val question: String,
)

data class ChatResponse(
        val threadId: Long,
        val question: String,
        val answer: String,
        val createdAt: LocalDateTime
)

data class ChatCompletionRequest(
        val model: String,
        val messages: List<Message>,
        val stream: Boolean = false
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ChatCompletionResponse(
        val id: String,
        val choices: List<Choice>
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Choice(
        val message: Message
)
