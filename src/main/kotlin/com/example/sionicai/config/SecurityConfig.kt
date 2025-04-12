package com.example.sionicai.config

import com.example.sionicai.auth.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity
class SecurityConfig(
        private val jwtFilter: JwtFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .csrf { it.disable() }

                .headers { it.frameOptions { frame -> frame.disable() } }

                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

                .authorizeHttpRequests {
                    it
                            .requestMatchers(
                                    "/api/auth/**",
                                    "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                                    "/h2-console/**"
                            ).permitAll()
                            .anyRequest().authenticated()
                }

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager =
            authConfig.authenticationManager
}
