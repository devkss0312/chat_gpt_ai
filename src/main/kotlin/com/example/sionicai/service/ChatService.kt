package com.example.sionicai.service

import com.example.sionicai.chat.OpenAiService
import com.example.sionicai.dto.ChatRequest
import com.example.sionicai.dto.ChatResponse
import com.example.sionicai.dto.Message
import com.example.sionicai.entity.Chat
import com.example.sionicai.entity.Thread
import com.example.sionicai.entity.User
import com.example.sionicai.repository.ChatRepository
import com.example.sionicai.repository.ThreadRepository
import com.example.sionicai.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatService(
        private val threadRepository: ThreadRepository,
        private val chatRepository: ChatRepository,
        private val userRepository: UserRepository,
        private val openAiService: OpenAiService // ← 이건 나중에 붙일 부분
) {

    fun chat(user: User, request: ChatRequest): ChatResponse {
        val lastThread = threadRepository.findTopByUserOrderByCreatedAtDesc(user)
        val now = LocalDateTime.now()

        val validThread = if (
                lastThread == null ||
                chatRepository.findAllByThreadOrderByCreatedAtAsc(lastThread)
                        .lastOrNull()?.createdAt?.isBefore(now.minusMinutes(30)) != false
        ) {
            threadRepository.save(Thread(user = user))
        } else lastThread

        val messages = chatRepository.findAllByThreadOrderByCreatedAtAsc(validThread).flatMap {
            listOf(
                    Message("user", it.question),
                    Message("assistant", it.answer)
            )
        } + Message("user", request.question)

        val answer = openAiService.ask(messages, "gpt-3.5-turbo", false)

        val saved = chatRepository.save(Chat(
                thread = validThread,
                question = request.question,
                answer = answer
        ))

        return ChatResponse(validThread.id, saved.question, saved.answer, saved.createdAt)
    }
}
