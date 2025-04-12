package com.example.sionicai.controller

import com.example.sionicai.dto.ChatRequest
import com.example.sionicai.dto.ChatResponse
import com.example.sionicai.entity.User
import com.example.sionicai.service.ChatService
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatController(
        private val chatService: ChatService
) {

    @PostMapping

    fun ask(
            @RequestBody request: ChatRequest,
            @AuthenticationPrincipal user: User
    ): ResponseEntity<ChatResponse> {
        val response = chatService.chat(user, request)
        return ResponseEntity.ok(response)
    }

}
