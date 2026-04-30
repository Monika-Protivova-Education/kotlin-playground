package space.harbour.kotlin.auth.domain

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ApiKeyAuthServiceImpl(
    @param:Value("\${security.api-keys}") private val validApiKeys: List<String>,
) : AuthService {

    override fun login(username: String, password: String): AuthResponse {
        // For API Key auth, the "password" field carries the API key
        val valid = password in validApiKeys
        return AuthResponse.ApiKeyResponse(
            apiKey = password,
            valid = valid,
        )
    }
}
