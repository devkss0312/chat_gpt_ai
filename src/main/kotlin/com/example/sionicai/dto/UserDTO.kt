package com.example.sionicai.dto

data class UserActivityResponse(
        val signupCount: Long,
        val loginCount: Long,
        val chatCount: Long
)