package space.harbour.kotlin.auth.application

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import space.harbour.kotlin.auth.domain.ApiKeyAuthServiceImpl
import space.harbour.kotlin.auth.domain.AuthResponse
import space.harbour.kotlin.auth.domain.BasicAuthServiceImpl
import space.harbour.kotlin.auth.domain.JwtAuthServiceImpl

@RestController
@RequestMapping("/auth")
class AuthController(
    private val basicAuthService: BasicAuthServiceImpl,
    private val jwtAuthService: JwtAuthServiceImpl,
    private val apiKeyAuthService: ApiKeyAuthServiceImpl,
) {

    @PostMapping("/login/basic")
    fun loginBasic(@RequestBody request: LoginRequest): AuthResponse =
        basicAuthService.login(request.username, request.password)

    @PostMapping("/login/jwt")
    fun loginJwt(@RequestBody request: LoginRequest): AuthResponse =
        jwtAuthService.login(request.username, request.password)

    @PostMapping("/login/api-key")
    fun validateApiKey(@RequestBody request: ApiKeyRequest): AuthResponse =
        apiKeyAuthService.login("", request.apiKey)
}

data class ApiKeyRequest(val apiKey: String)
