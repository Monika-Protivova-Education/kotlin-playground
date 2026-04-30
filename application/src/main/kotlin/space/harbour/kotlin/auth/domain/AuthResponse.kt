package space.harbour.kotlin.auth.domain

sealed class AuthResponse {
    data class TokenResponse(val token: String) : AuthResponse()
    data class UserResponse(val username: String, val role: String) : AuthResponse()
    data class ApiKeyResponse(val apiKey: String, val valid: Boolean) : AuthResponse()
    data class OAuth2Info(val issuer: String, val message: String) : AuthResponse()
}
