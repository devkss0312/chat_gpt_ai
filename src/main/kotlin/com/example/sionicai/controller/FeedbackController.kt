package com.example.sionicai.controller

import com.example.sionicai.dto.FeedbackRequest
import com.example.sionicai.dto.FeedbackResponse
import com.example.sionicai.entity.FeedbackStatus
import com.example.sionicai.entity.Role
import com.example.sionicai.entity.User
import com.example.sionicai.service.FeedbackService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/feedback")
@SecurityRequirement(name = "BearerAuth")
class FeedbackController(
        private val feedbackService: FeedbackService
) {

    @PostMapping
    fun createFeedback(
            @AuthenticationPrincipal user: User,
            @RequestBody request: FeedbackRequest
    ): ResponseEntity<FeedbackResponse> {
        return ResponseEntity.ok(feedbackService.createFeedback(user, request))
    }

    @GetMapping
    fun getFeedbacks(
            @AuthenticationPrincipal user: User,
            @RequestParam(defaultValue = "0") page: Int,
            @RequestParam(defaultValue = "10") size: Int,
            @RequestParam(defaultValue = "desc") sort: String,
            @RequestParam(required = false) positive: Boolean?
    ): ResponseEntity<Page<FeedbackResponse>> {
        return ResponseEntity.ok(feedbackService.getFeedbacks(user, page, size, sort, positive))
    }

    @PatchMapping("/{id}")
    fun updateStatus(
            @AuthenticationPrincipal user: User,
            @PathVariable id: Long,
            @RequestParam status: FeedbackStatus
    ): ResponseEntity<FeedbackResponse> {
        if (user.role != Role.ADMIN) return ResponseEntity.status(403).build()
        return ResponseEntity.ok(feedbackService.updateStatus(user, id, status))
    }
}
