package com.example.sionicai.repository

import com.example.sionicai.entity.Feedback
import com.example.sionicai.entity.FeedbackStatus
import com.example.sionicai.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun existsByUserIdAndChatId(userId: Long, chatId: Long): Boolean
    fun findByUser(user: User, pageable: Pageable): Page<Feedback>
    fun findAllByPositive(positive: Boolean, pageable: Pageable): Page<Feedback>
    fun findByStatus(status: FeedbackStatus, pageable: Pageable): Page<Feedback>
    fun findByUserIdAndChatId(userId: Long, chatId: Long): Feedback?
}