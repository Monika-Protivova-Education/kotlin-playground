package space.harbour.kotlin.security

import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import space.harbour.kotlin.auth.domain.ApiKeyAuthServiceImpl
import space.harbour.kotlin.auth.domain.BasicAuthServiceImpl
import space.harbour.kotlin.auth.domain.JwtAuthServiceImpl
import space.harbour.kotlin.security.apikey.ApiKeyAuthenticationFilter
import space.harbour.kotlin.security.jwt.JwtService
import space.harbour.kotlin.task.domain.TaskService
import space.harbour.kotlin.user.domain.BasicAuthUserDetailsService
import space.harbour.kotlin.user.domain.User
import space.harbour.kotlin.user.domain.UserRepository
import space.harbour.kotlin.user.domain.UserRole
import space.harbour.kotlin.user.domain.UserService

@TestConfiguration
class SecurityTestConfig {

    @Bean
    fun taskService(): TaskService = mockk(relaxed = true)

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userRepository(): UserRepository {
        val encoder = BCryptPasswordEncoder()
        val testUser = User(id = 1L, username = "user", password = encoder.encode("password"), role = UserRole.USER)
        val testAdmin = User(id = 2L, username = "admin", password = encoder.encode("admin"), role = UserRole.ADMIN)

        return mockk<UserRepository>().also {
            every { it.findByUsername("user") } returns testUser
            every { it.findByUsername("admin") } returns testAdmin
            every { it.findByUsername(match { it != "user" && it != "admin" }) } returns null
        }
    }

    @Bean
    fun userService(userRepository: UserRepository): UserService = UserService(userRepository)

    @Bean
    fun basicAuthUserDetailsService(userRepository: UserRepository): BasicAuthUserDetailsService =
        BasicAuthUserDetailsService(userRepository)

    @Bean
    fun jwtService(): JwtService = JwtService(
        secret = "test-secret-key-that-is-at-least-256-bits-long-for-hs256",
        expirationMs = 3600000,
    )

    @Bean
    fun apiKeyAuthenticationFilter(): ApiKeyAuthenticationFilter =
        ApiKeyAuthenticationFilter(listOf("test-key-1", "test-key-2"))

    @Bean
    fun basicAuthService(userRepository: UserRepository, passwordEncoder: PasswordEncoder): BasicAuthServiceImpl =
        BasicAuthServiceImpl(userRepository, passwordEncoder)

    @Bean
    fun jwtAuthService(userRepository: UserRepository, passwordEncoder: PasswordEncoder, jwtService: JwtService): JwtAuthServiceImpl =
        JwtAuthServiceImpl(userRepository, passwordEncoder, jwtService)

    @Bean
    fun apiKeyAuthService(): ApiKeyAuthServiceImpl =
        ApiKeyAuthServiceImpl(listOf("test-key-1", "test-key-2"))
}
