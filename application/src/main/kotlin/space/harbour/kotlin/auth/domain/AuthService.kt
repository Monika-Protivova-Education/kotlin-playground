package space.harbour.kotlin.auth.domain

interface AuthService {
    fun login(username: String, password: String): AuthResponse
}
