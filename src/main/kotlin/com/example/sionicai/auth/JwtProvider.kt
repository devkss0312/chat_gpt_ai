
package com.example.sionicai.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.sionicai.entity.User
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtProvider(
        private val jwtProperties: JwtProperties
) {
    fun createToken(user: User): String {
        val now = Date()
        val expiry = Date(now.time + jwtProperties.expiration)

        return JWT.create()
                .withSubject(user.email)
                .withClaim("id", user.id)
                .withClaim("role", user.role.name)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(Algorithm.HMAC512(jwtProperties.secret))
    }
}
