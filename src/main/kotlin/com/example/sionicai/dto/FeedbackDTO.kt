package com.example.sionicai.dto


import com.example.sionicai.entity.Feedback
import com.example.sionicai.entity.FeedbackStatus
import java.time.LocalDateTime



data class FeedbackRequest(
        val chatId: Long,
        val positive: Boolean,
        val content: String = ""
)


data class FeedbackResponse(
        val id: Long,
        val chatId: Long,
        val userId: Long,
        val positive: Boolean,
        val content: String,
        val status: FeedbackStatus,
        val createdAt: LocalDateTime
) {
    companion object {
        fun from(feedback: Feedback): FeedbackResponse = FeedbackResponse(
                id = feedback.id,
                chatId = feedback.chat.id,
                userId = feedback.user.id,
                positive = feedback.positive,
                content = feedback.content,
                status = feedback.status,
                createdAt = feedback.createdAt
        )
    }
}
