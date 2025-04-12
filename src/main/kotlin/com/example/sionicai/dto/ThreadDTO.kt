package com.example.sionicai.dto

import com.example.sionicai.entity.Chat
import com.example.sionicai.entity.Thread
import java.time.LocalDateTime

data class ThreadResponse(
        val id: Long,
        val createdAt: LocalDateTime,
        val chats: List<ChatDto>
) {
    companion object {
        fun from(thread: Thread): ThreadResponse {
            return ThreadResponse(
                    id = thread.id,
                    createdAt = thread.createdAt,
                    chats = thread.chats.sortedBy { it.createdAt }.map { ChatDto.from(it) }
            )
        }
    }
}
