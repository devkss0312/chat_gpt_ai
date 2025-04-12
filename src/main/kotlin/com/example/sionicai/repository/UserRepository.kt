package com.example.sionicai.repository

import com.example.sionicai.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun countByCreatedAtBetween(from: LocalDateTime, to: LocalDateTime): Long

}
