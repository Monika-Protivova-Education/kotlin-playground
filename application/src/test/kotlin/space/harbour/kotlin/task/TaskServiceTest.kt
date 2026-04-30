package space.harbour.kotlin.task

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import space.harbour.kotlin.error.AppError
import space.harbour.kotlin.user.domain.UserService
import space.harbour.kotlin.task.domain.Task
import space.harbour.kotlin.task.domain.TaskRepository
import space.harbour.kotlin.task.domain.TaskService
import space.harbour.kotlin.task.domain.TaskServiceImpl
import space.harbour.kotlin.task.domain.TaskStatus

class TaskServiceTest : FunSpec({

    val taskRepository: TaskRepository = mockk(relaxed = true)
    val userService: UserService = mockk()

    val taskService: TaskService = TaskServiceImpl(
        taskRepository = taskRepository,
        userService = userService,
    )

    beforeEach {
        clearAllMocks()
        every { userService.getCurrentUserIdOrThrow() } returns 1L
    }

    test("getTask should return existing task") {

        val expected = Task(
            id = 42,
            description = "Get the answer",
            status = TaskStatus.IN_PROGRESS,
            createdBy = 1,
        )

        every {
            taskRepository.findTask(userId = 1L, taskId = 42L)
        } returns expected

        taskService.getTask(42L).shouldBe(expected)

        verify(exactly = 1) {
            taskRepository.findTask(userId = 1L, taskId = 42L)
        }
    }

    test("getTask should return error when task does not exist") {

        every {
            taskRepository.findTask(userId = 1L, taskId = 42L)
        } throws AppError.TaskNotFound(42L)

        shouldThrow<AppError> {
            taskService.getTask(42L)
        }

        verify(exactly = 1) {
            taskRepository.findTask(userId = 1L, taskId = 42L)
        }

        verify(exactly = 0) {
            taskRepository.updateTask(any())
        }
    }

})
