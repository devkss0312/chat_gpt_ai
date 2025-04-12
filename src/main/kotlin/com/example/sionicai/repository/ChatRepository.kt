package com.example.sionicai.repository

import com.example.sionicai.entity.Chat
import com.example.sionicai.entity.Thread
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ChatRepository : JpaRepository<Chat, Long> {
    fun findAllByThreadOrderByCreatedAtAsc(thread: Thread): List<Chat>
    fun countByCreatedAtBetween(from: LocalDateTime, to: LocalDateTime): Long
    fun findAllByCreatedAtBetween(from: LocalDateTime, to: LocalDateTime): List<Chat>

}