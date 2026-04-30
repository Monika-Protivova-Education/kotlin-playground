package space.harbour.kotlin.task.infrastructure.jooq

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import space.harbour.kotlin.Profiles
import org.springframework.transaction.annotation.Transactional
import space.harbour.kotlin.task.domain.Task
import space.harbour.kotlin.task.domain.TaskRepository
import space.harbour.kotlin.task.domain.TaskStatus
import space.harbour.kotlin.task.infrastructure.PostgresTestContainerConfig

@SpringBootTest
@ActiveProfiles(Profiles.JOOQ, Profiles.TEST)
@Transactional
@Import(PostgresTestContainerConfig::class)
class JooqTaskRepositoryImplTest : FunSpec() {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    private val userId = 1L

    init {
        test("insertTask should persist and return task") {
            val task = Task(id = 100, description = "New task", status = TaskStatus.NOT_STARTED, createdBy = userId)

            val saved = taskRepository.insertTask(task)

            saved.description shouldBe "New task"
            saved.status shouldBe TaskStatus.NOT_STARTED

            val found = taskRepository.findTask(userId = userId, taskId = saved.id)
            found.description shouldBe "New task"
        }

        test("findTasks should return all tasks for user when no filters") {
            taskRepository.insertTask(Task(id = 200, description = "Task A", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 201, description = "Task B", status = TaskStatus.IN_PROGRESS, createdBy = userId))

            val all = taskRepository.findTasks(userId = userId)
            all shouldHaveSize 2
        }

        test("findTasks should not return tasks from other users") {
            taskRepository.insertTask(Task(id = 250, description = "My task", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 251, description = "Other task", status = TaskStatus.NOT_STARTED, createdBy = 99L))

            val myTasks = taskRepository.findTasks(userId = userId)
            myTasks shouldHaveSize 1
            myTasks.first().description shouldBe "My task"
        }

        test("findTasks should filter by status") {
            taskRepository.insertTask(Task(id = 300, description = "Task A", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 301, description = "Task B", status = TaskStatus.IN_PROGRESS, createdBy = userId))
            taskRepository.insertTask(Task(id = 302, description = "Task C", status = TaskStatus.NOT_STARTED, createdBy = userId))

            val notStarted = taskRepository.findTasks(userId = userId, status = TaskStatus.NOT_STARTED)
            notStarted shouldHaveSize 2
            notStarted.map { it.description } shouldContainExactlyInAnyOrder listOf("Task A", "Task C")
        }

        test("findTasks should filter by startsWith") {
            taskRepository.insertTask(Task(id = 400, description = "Complete homework", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 401, description = "Clean the house", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 402, description = "Complete assignment", status = TaskStatus.NOT_STARTED, createdBy = userId))

            val matching = taskRepository.findTasks(userId = userId, startsWith = "Complete")
            matching shouldHaveSize 2
            matching.map { it.description } shouldContainExactlyInAnyOrder listOf("Complete homework", "Complete assignment")
        }

        test("findTask should find by id for user") {
            taskRepository.insertTask(Task(id = 500, description = "Specific task", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 501, description = "Other task", status = TaskStatus.NOT_STARTED, createdBy = userId))

            val found = taskRepository.findTask(userId = userId, taskId = 500)
            found.description shouldBe "Specific task"
        }

        test("updateTask should modify existing task") {
            taskRepository.insertTask(Task(id = 600, description = "Original", status = TaskStatus.NOT_STARTED, createdBy = userId))

            taskRepository.updateTask(Task(id = 600, description = "Updated", status = TaskStatus.IN_PROGRESS, createdBy = userId))

            val found = taskRepository.findTask(userId = userId, taskId = 600)
            found.description shouldBe "Updated"
            found.status shouldBe TaskStatus.IN_PROGRESS
        }

        test("deleteTask should remove the task") {
            taskRepository.insertTask(Task(id = 700, description = "To delete", status = TaskStatus.NOT_STARTED, createdBy = userId))

            taskRepository.deleteTask(700)

            taskRepository.findTasks(userId = userId).shouldBeEmpty()
        }
    }
}
