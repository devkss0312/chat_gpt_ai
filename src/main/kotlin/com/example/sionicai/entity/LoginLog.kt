package com.example.sionicai.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "login_logs")
class LoginLog(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        val user: User,

        @Column(nullable = false)
        val timestamp: LocalDateTime = LocalDateTime.now()
)
