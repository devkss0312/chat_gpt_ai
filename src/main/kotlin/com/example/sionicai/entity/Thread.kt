package com.example.sionicai.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "threads")
class Thread(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        val user: User,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now(),

        @OneToMany(mappedBy = "thread", cascade = [CascadeType.ALL], orphanRemoval = true)
        val chats: List<Chat> = mutableListOf()
) {
    protected constructor() : this(user = User(name = "", email = "", password = ""))
}
