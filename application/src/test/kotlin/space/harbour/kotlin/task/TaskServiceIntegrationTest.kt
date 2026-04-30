package space.harbour.kotlin.task

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import space.harbour.kotlin.Profiles
import org.springframework.transaction.annotation.Transactional
import space.harbour.kotlin.error.AppError
import space.harbour.kotlin.task.domain.Task
import space.harbour.kotlin.task.domain.TaskService
import space.harbour.kotlin.task.domain.TaskStatus
import space.harbour.kotlin.task.infrastructure.PostgresTestContainerConfig
import space.harbour.kotlin.user.domain.UserRole
import space.harbour.kotlin.user.infrastructure.jpa.JpaUserEntity
import space.harbour.kotlin.user.infrastructure.jpa.SpringDataUserRepository

@SpringBootTest
@ActiveProfiles(Profiles.JDBC, Profiles.TEST)
@Transactional
@Import(PostgresTestContainerConfig::class)
@WithMockUser(username = "testuser")
class TaskServiceIntegrationTest : FunSpec() {

    @Autowired
    private lateinit var taskService: TaskService

    @Autowired
    private lateinit var userRepository: SpringDataUserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    init {

        test("getTask should return existing task") {
            val user = userRepository.save(
                JpaUserEntity(username = "testuser", password = passwordEncoder.encode("pass"), role = UserRole.USER)
            )

            val added = taskService.addTask(
                Task(
                    id = 0,
                    description = "Get the answer",
                    status = TaskStatus.IN_PROGRESS,
                    createdBy = user.id!!,
                )
            )

            taskService.getTask(added.id) shouldBe added
        }

        test("getTask should return error when task does not exist") {
            userRepository.save(
                JpaUserEntity(username = "testuser", password = passwordEncoder.encode("pass"), role = UserRole.USER)
            )

            shouldThrow<AppError> {
                taskService.getTask(9999L)
            }
        }

    }
}
