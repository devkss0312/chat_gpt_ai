
package com.example.sionicai.service

import com.example.sionicai.dto.FeedbackRequest
import com.example.sionicai.dto.FeedbackResponse
import com.example.sionicai.entity.Feedback
import com.example.sionicai.entity.FeedbackStatus
import com.example.sionicai.entity.Role
import com.example.sionicai.entity.User
import com.example.sionicai.repository.ChatRepository
import com.example.sionicai.repository.FeedbackRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class FeedbackService(
        private val feedbackRepository: FeedbackRepository,
        private val chatRepository: ChatRepository
) {
    fun createFeedback(user: User, request: FeedbackRequest): FeedbackResponse {
        val chat = chatRepository.findById(request.chatId).orElseThrow { IllegalArgumentException("채팅이 존재하지 않습니다.") }

        if (user.role != Role.ADMIN && chat.thread.user.id != user.id) {
            throw IllegalAccessException("본인의 대화에만 피드백을 남길 수 있습니다.")
        }

        val existing = feedbackRepository.findByUserIdAndChatId(user.id, request.chatId)
        val feedback = if (existing != null) {
            existing.positive = request.positive
            existing.content = request.content
            existing
        } else {
            Feedback(
                    user = user,
                    chat = chat,
                    positive = request.positive,
                    content = request.content
            )
        }
        return FeedbackResponse.from(feedbackRepository.save(feedback))
    }

    fun getFeedbacks(user: User, page: Int, size: Int, sort: String, positive: Boolean?): Page<FeedbackResponse> {
        val pageable = PageRequest.of(page, size, if (sort == "asc") Sort.by("createdAt").ascending() else Sort.by("createdAt").descending())

        val feedbacks = when {
            user.role == Role.ADMIN && positive != null -> feedbackRepository.findAllByPositive(positive, pageable)
            user.role == Role.ADMIN -> feedbackRepository.findAll(pageable)
            else -> feedbackRepository.findByUser(user, pageable)
        }

        return feedbacks.map { FeedbackResponse.from(it) }
    }

    fun updateStatus(user: User, feedbackId: Long, status: FeedbackStatus): FeedbackResponse {
        val feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow { IllegalArgumentException("피드백이 존재하지 않습니다.") }

        if (user.role != Role.ADMIN) {
            throw IllegalAccessException("관리자만 상태를 변경할 수 있습니다.")
        }

        feedback.status = status
        return FeedbackResponse.from(feedbackRepository.save(feedback))
    }
}