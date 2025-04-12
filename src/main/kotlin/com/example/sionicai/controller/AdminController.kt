package com.example.sionicai.controller

import com.example.sionicai.dto.UserActivityResponse
import com.example.sionicai.entity.Role
import com.example.sionicai.entity.User
import com.example.sionicai.repository.ChatRepository
import com.example.sionicai.repository.LoginLogRepository
import com.example.sionicai.repository.UserRepository
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayOutputStream
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "BearerAuth")
class AdminController(
        private val userRepository: UserRepository,
        private val chatRepository: ChatRepository,
        private val loginLogRepository: LoginLogRepository
) {

    @GetMapping("/stats")
    fun getStats(
            @AuthenticationPrincipal user: User
    ): ResponseEntity<UserActivityResponse> {
        if (user.role != Role.ADMIN) return ResponseEntity.status(403).build()

        val from = LocalDateTime.now().minusDays(1)
        val to = LocalDateTime.now()

        val signupCount = userRepository.countByCreatedAtBetween(from, to)
        val loginCount = loginLogRepository.countByTimestampBetween(from, to)
        val chatCount = chatRepository.countByCreatedAtBetween(from, to)

        return ResponseEntity.ok(UserActivityResponse(signupCount, loginCount, chatCount))
    }

    @GetMapping("/report")
    fun generateReport(
            @AuthenticationPrincipal user: User
    ): ResponseEntity<ByteArrayResource> {
        if (user.role != Role.ADMIN) return ResponseEntity.status(403).build()

        val from = LocalDateTime.now().minusDays(1)
        val to = LocalDateTime.now()
        val chats = chatRepository.findAllByCreatedAtBetween(from, to)

        val outputStream = ByteArrayOutputStream()
        val writer = PrintWriter(outputStream, true, StandardCharsets.UTF_8)

        writer.println("user_email,question,answer,created_at")
        chats.forEach {
            writer.println("\"${it.thread.user.email}\",\"${it.question}\",\"${it.answer}\",\"${it.createdAt}\")")
        }
        writer.flush()

        val resource = ByteArrayResource(outputStream.toByteArray())

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=chat_report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource)
    }
}
