package com.example.sionicai.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
        name = "feedbacks",
        uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "chat_id"])]
)
class Feedback(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        val user: User,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "chat_id", nullable = false)
        val chat: Chat,

        @Column(nullable = false)
        var positive: Boolean,

        @Column(nullable = false, columnDefinition = "TEXT")
        var content: String = "",

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        var status: FeedbackStatus = FeedbackStatus.PENDING,

        @Column(nullable = false)
        val createdAt: LocalDateTime = LocalDateTime.now()
) {
        protected constructor() : this(
                user = User(name = "", email = "", password = ""),
                chat = Chat(
                        thread = Thread(user = User(name = "", email = "", password = "")),
                        question = "",
                        answer = ""
                ),
                positive = true
        )
}

enum class FeedbackStatus {
        PENDING, RESOLVED
}
