package space.harbour.kotlin.task

import io.kotest.core.spec.style.FunSpec
import io.mockk.clearMocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import space.harbour.kotlin.security.SecurityConfig
import space.harbour.kotlin.security.SecurityTestConfig
import space.harbour.kotlin.task.application.TaskController
import space.harbour.kotlin.task.domain.TaskService

@WebMvcTest(TaskController::class)
@AutoConfigureMockMvc
@Import(SecurityConfig::class, SecurityTestConfig::class)
@TestPropertySource(properties = [
    "security.jwt.secret=test-secret-key-that-is-at-least-256-bits-long-for-hs256",
    "security.jwt.expiration-ms=3600000",
    "security.api-keys=test-key-1,test-key-2",
])
class TaskControllerTest : FunSpec() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var taskService: TaskService

    init {
        beforeEach {
            clearMocks(taskService)
        }

        test("controller is loaded") {
            // MockMvc and controller are auto-configured
        }
    }
}
