package com.example.sionicai.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.sionicai.entity.User
import com.example.sionicai.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
        private val userRepository: UserRepository,
        private val jwtProperties: JwtProperties
) : OncePerRequestFilter() {

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        val token = getTokenFromRequest(request)

        if (!token.isNullOrBlank()) {
            try {
                val decodedJWT = JWT.require(Algorithm.HMAC512(jwtProperties.secret))
                        .build()
                        .verify(token)

                val email = decodedJWT.subject
                val user = userRepository.findByEmail(email)

                if (user != null) {
                    val auth = UsernamePasswordAuthenticationToken(
                            user, null, listOf()
                    )
                    auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = auth
                }
            } catch (e: Exception) {
                logger.warn("JWT 검증 실패: ${e.message}")
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return if (header.startsWith(jwtProperties.prefix)) {
            header.removePrefix(jwtProperties.prefix).trim()
        } else null
    }
}
