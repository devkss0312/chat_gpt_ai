package com.example.sionicai.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val authService: AuthService
) {

    @PostMapping("/signup")
    fun signup(@RequestBody req: SignupRequest): ResponseEntity<String> {
        authService.signup(req)
        return ResponseEntity.ok("회원가입 성공")
    }

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<AuthResponse> {
        val token = authService.login(req)
        return ResponseEntity.ok(AuthResponse(token))
    }
}
