package com.example.sionicai.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(
        val role: String,
        val content: String
)
