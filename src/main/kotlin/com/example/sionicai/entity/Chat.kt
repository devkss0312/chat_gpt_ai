package com.example.sionicai.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chats")
class Chat(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        val thread: Thread,

        @Column(nullable = false, columnDefinition = "TEXT")
        val question: String,

        @Column(nullable = false, columnDefinition = "TEXT")
        val answer: String,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
) {
    protected constructor() : this(thread = Thread(user = User(name = "", email = "", password = "")), question = "", answer = "")
}
