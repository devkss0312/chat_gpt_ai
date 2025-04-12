package com.example.sionicai.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false, unique = true)
        val email: String,

        @Column(nullable = false)
        val password: String,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        val role: Role = Role.MEMBER,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
) {
        protected constructor() : this(
                id = 0L,
                email = "",
                password = "",
                name = "",
                role = Role.MEMBER,
                createdAt = LocalDateTime.now()
        )
}


enum class Role {
    MEMBER, ADMIN
}
