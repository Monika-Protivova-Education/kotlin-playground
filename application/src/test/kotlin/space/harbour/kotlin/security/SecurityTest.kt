package space.harbour.kotlin.security

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import space.harbour.kotlin.auth.application.AuthController
import space.harbour.kotlin.security.jwt.JwtService
import space.harbour.kotlin.task.application.TaskController

@WebMvcTest(controllers = [TaskController::class, AuthController::class])
@Import(SecurityConfig::class, SecurityTestConfig::class)
@TestPropertySource(properties = [
    "security.jwt.secret=test-secret-key-that-is-at-least-256-bits-long-for-hs256",
    "security.jwt.expiration-ms=3600000",
    "security.api-keys=test-key-1,test-key-2",
])
class SecurityTest : FunSpec() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var jwtService: JwtService

    init {
        // --- No auth ---

        test("GET /tasks without any credentials returns 401") {
            mockMvc.perform(get("/tasks"))
                .andExpect(status().isUnauthorized)
        }

        // --- Basic Auth ---

        test("GET /tasks with valid Basic Auth returns 200") {
            mockMvc.perform(get("/tasks").with(httpBasic("user", "password")))
                .andExpect(status().isOk)
        }

        test("GET /tasks with wrong Basic Auth password returns 401") {
            mockMvc.perform(get("/tasks").with(httpBasic("user", "wrong")))
                .andExpect(status().isUnauthorized)
        }

        test("POST /auth/login/basic with valid credentials returns user info") {
            mockMvc.perform(
                post("/auth/login/basic")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"username":"user","password":"password"}""")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.role").value("USER"))
        }

        // --- JWT ---

        test("POST /auth/login/jwt with valid credentials returns token") {
            mockMvc.perform(
                post("/auth/login/jwt")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"username":"user","password":"password"}""")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.token").exists())
        }

        test("POST /auth/login/jwt with invalid credentials returns 401") {
            mockMvc.perform(
                post("/auth/login/jwt")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"username":"user","password":"wrong"}""")
            )
                .andExpect(status().isUnauthorized)
        }

        test("GET /tasks with valid JWT Bearer token returns 200") {
            val token = jwtService.generateToken("user")
            token shouldNotBe null

            mockMvc.perform(
                get("/tasks").header("Authorization", "Bearer $token")
            )
                .andExpect(status().isOk)
        }

        test("GET /tasks with invalid Bearer token returns 401") {
            mockMvc.perform(
                get("/tasks").header("Authorization", "Bearer invalid-token")
            )
                .andExpect(status().isUnauthorized)
        }

        // --- API Key ---

        test("GET /tasks with valid X-API-Key returns 200") {
            mockMvc.perform(get("/tasks").header("X-API-Key", "test-key-1"))
                .andExpect(status().isOk)
        }

        test("GET /tasks with invalid X-API-Key returns 401") {
            mockMvc.perform(get("/tasks").header("X-API-Key", "invalid-key"))
                .andExpect(status().isUnauthorized)
        }

        test("POST /auth/login/api-key validates API key") {
            mockMvc.perform(
                post("/auth/login/api-key")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""{"apiKey":"test-key-1"}""")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.apiKey").value("test-key-1"))
                .andExpect(jsonPath("$.valid").value(true))
        }
    }
}
