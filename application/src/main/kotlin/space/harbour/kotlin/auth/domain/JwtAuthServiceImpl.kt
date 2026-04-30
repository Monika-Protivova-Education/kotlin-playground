package space.harbour.kotlin.auth.domain

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import space.harbour.kotlin.error.AppError
import space.harbour.kotlin.security.jwt.JwtService
import space.harbour.kotlin.user.domain.UserRepository

@Service
class JwtAuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
) : AuthService {

    override fun login(username: String, password: String): AuthResponse {
        val user = userRepository.findByUsername(username)
            ?: throw AppError.Unauthorized("Invalid credentials")

        if (!passwordEncoder.matches(password, user.password)) {
            throw AppError.Unauthorized("Invalid credentials")
        }

        val token = jwtService.generateToken(user.username)
        return AuthResponse.TokenResponse(token = token)
    }
}
