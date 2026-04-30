package space.harbour.kotlin.auth.domain

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import space.harbour.kotlin.error.AppError
import space.harbour.kotlin.user.domain.UserRepository

@Service
class BasicAuthServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : AuthService {

    override fun login(username: String, password: String): AuthResponse {
        val user = userRepository.findByUsername(username)
            ?: throw AppError.Unauthorized("Invalid credentials")

        if (!passwordEncoder.matches(password, user.password)) {
            throw AppError.Unauthorized("Invalid credentials")
        }

        return AuthResponse.UserResponse(
            username = user.username,
            role = user.role.name,
        )
    }
}
