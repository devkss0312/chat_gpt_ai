package com.example.sionicai.config

import com.example.sionicai.entity.Role
import com.example.sionicai.entity.User
import com.example.sionicai.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import jakarta.annotation.PostConstruct
import java.time.LocalDateTime

@Component
class AdminAccountInitializer(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
) {

    @PostConstruct
    fun createAdmin() {
        val adminEmail = "admin@example.com"
        val admin = User(
                email = adminEmail,
                password = passwordEncoder.encode("1234"),
                name = "관리자",
                role = Role.ADMIN,
                createdAt = LocalDateTime.now()
        )
        userRepository.save(admin)
        println("✅ 관리자 계정 생성됨: $adminEmail / 비번: 1234")
    }
}