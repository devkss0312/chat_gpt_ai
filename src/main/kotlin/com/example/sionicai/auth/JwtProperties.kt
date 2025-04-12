
package com.example.sionicai.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtProperties(
        @Value("\${jwt.secret}")
        val secret: String,

        @Value("\${jwt.expiration:3600000}")
        val expiration: Long
) {
    val header = "Authorization"
    val prefix = "Bearer "
}

