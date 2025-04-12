package com.example.sionicai.repository

import com.example.sionicai.entity.LoginLog
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface LoginLogRepository : JpaRepository<LoginLog, Long> {
    fun countByTimestampBetween(from: LocalDateTime, to: LocalDateTime): Long
}
