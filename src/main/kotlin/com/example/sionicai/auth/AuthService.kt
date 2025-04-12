package com.example.sionicai.auth

import com.example.sionicai.entity.LoginLog
import com.example.sionicai.entity.Role
import com.example.sionicai.entity.User
import com.example.sionicai.repository.LoginLogRepository
import com.example.sionicai.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
        private val userRepository: UserRepository,
        private val passwordEncoder: BCryptPasswordEncoder,
        private val jwtProvider: JwtProvider,
        private val loginLogRepository: LoginLogRepository
) {

    fun signup(req: SignupRequest): User {
        if (userRepository.findByEmail(req.email) != null) {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }

        val user = User(
                email = req.email,
                password = passwordEncoder.encode(req.password),
                name = req.name,
                role = Role.MEMBER
        )
        return userRepository.save(user)
    }

    fun login(req: LoginRequest): String {
        val user = userRepository.findByEmail(req.email)
                ?: throw IllegalArgumentException("존재하지 않는 이메일입니다.")

        if (!passwordEncoder.matches(req.password, user.password)) {
            throw IllegalArgumentException("비밀번호가 일치하지 않습니다.")
        }

        loginLogRepository.save(LoginLog(user = user))

        return jwtProvider.createToken(user)
    }
}