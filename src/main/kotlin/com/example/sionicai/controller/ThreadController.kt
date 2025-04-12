package com.example.sionicai.controller

import com.example.sionicai.dto.ThreadResponse
import com.example.sionicai.entity.Role
import com.example.sionicai.entity.User
import com.example.sionicai.repository.ThreadRepository
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/threads")
@SecurityRequirement(name = "BearerAuth")
class ThreadController(
        private val threadRepository: ThreadRepository
) {

    @GetMapping
    fun getThreads(
            @AuthenticationPrincipal user: User,
            @RequestParam(defaultValue = "0") page: Int,
            @RequestParam(defaultValue = "10") size: Int,
            @RequestParam(defaultValue = "desc") sort: String
    ): ResponseEntity<List<ThreadResponse>> {
        val sortOrder = if (sort == "asc") Sort.by("createdAt").ascending() else Sort.by("createdAt").descending()
        val pageable = PageRequest.of(page, size, sortOrder)

        val threads = if (user.role == Role.ADMIN) {
            threadRepository.findAll(pageable).content
        } else {
            threadRepository.findAllByUser(user, pageable)
        }

        val result = threads.map { ThreadResponse.from(it) }
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{id}")
    fun deleteThread(
            @AuthenticationPrincipal user: User,
            @PathVariable id: Long
    ): ResponseEntity<Void> {
        val thread = threadRepository.findById(id).orElseThrow { IllegalArgumentException("존재하지 않는 스레드입니다.") }

        if (user.role != Role.ADMIN && thread.user.id != user.id) {
            return ResponseEntity.status(403).build()
        }

        threadRepository.delete(thread)
        return ResponseEntity.noContent().build()
    }
}
