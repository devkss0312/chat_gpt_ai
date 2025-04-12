package com.example.sionicai.repository

import com.example.sionicai.entity.Thread
import com.example.sionicai.entity.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ThreadRepository : JpaRepository<Thread, Long> {
    fun findTopByUserOrderByCreatedAtDesc(user: User): Thread?
    fun findAllByUser(user: User, pageable: Pageable): List<Thread>
}