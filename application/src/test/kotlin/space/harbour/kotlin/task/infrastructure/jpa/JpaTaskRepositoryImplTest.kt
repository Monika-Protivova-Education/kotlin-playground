package space.harbour.kotlin.task.infrastructure.jpa

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
@ActiveProfiles(Profiles.JPA, Profiles.TEST)
@Transactional
@Import(PostgresTestContainerConfig::class)
class JpaTaskRepositoryImplTest : FunSpec() {

    @Autowired
    private lateinit var taskRepository: TaskRepository

    private val userId = 1L

    init {
        test("insertTask should persist and return task with generated id") {
            val task = Task(id = 0, description = "New task", status = TaskStatus.NOT_STARTED, createdBy = userId)

            val saved = taskRepository.insertTask(task)

            saved.description shouldBe "New task"
            saved.status shouldBe TaskStatus.NOT_STARTED
            saved.createdBy shouldBe userId

            val found = taskRepository.findTask(userId = userId, taskId = saved.id)
            found shouldBe saved
        }

        test("findTasks should return all tasks for user when no filters") {
            taskRepository.insertTask(Task(id = 0, description = "Task A", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 0, description = "Task B", status = TaskStatus.IN_PROGRESS, createdBy = userId))

            val all = taskRepository.findTasks(userId = userId)
            all shouldHaveSize 2
        }

        test("findTasks should not return tasks from other users") {
            taskRepository.insertTask(Task(id = 0, description = "My task", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 0, description = "Other task", status = TaskStatus.NOT_STARTED, createdBy = 99L))

            val myTasks = taskRepository.findTasks(userId = userId)
            myTasks shouldHaveSize 1
            myTasks.first().description shouldBe "My task"
        }

        test("findTasks should filter by status") {
            taskRepository.insertTask(Task(id = 0, description = "Task A", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 0, description = "Task B", status = TaskStatus.IN_PROGRESS, createdBy = userId))
            taskRepository.insertTask(Task(id = 0, description = "Task C", status = TaskStatus.NOT_STARTED, createdBy = userId))

            val notStarted = taskRepository.findTasks(userId = userId, status = TaskStatus.NOT_STARTED)
            notStarted shouldHaveSize 2
            notStarted.map { it.description } shouldContainExactlyInAnyOrder listOf("Task A", "Task C")
        }

        test("findTasks should filter by startsWith") {
            taskRepository.insertTask(Task(id = 0, description = "Complete homework", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 0, description = "Clean the house", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 0, description = "Complete assignment", status = TaskStatus.NOT_STARTED, createdBy = userId))

            val matching = taskRepository.findTasks(userId = userId, startsWith = "Complete")
            matching shouldHaveSize 2
            matching.map { it.description } shouldContainExactlyInAnyOrder listOf("Complete homework", "Complete assignment")
        }

        test("findTask should find by id for user") {
            val saved = taskRepository.insertTask(Task(id = 0, description = "Specific task", status = TaskStatus.NOT_STARTED, createdBy = userId))
            taskRepository.insertTask(Task(id = 0, description = "Other task", status = TaskStatus.NOT_STARTED, createdBy = userId))

            val found = taskRepository.findTask(userId = userId, taskId = saved.id)
            found.description shouldBe "Specific task"
        }

        test("updateTask should modify existing task") {
            val saved = taskRepository.insertTask(Task(id = 0, description = "Original", status = TaskStatus.NOT_STARTED, createdBy = userId))
            val updated = saved.copy(description = "Updated", status = TaskStatus.IN_PROGRESS)

            taskRepository.updateTask(updated)

            val found = taskRepository.findTask(userId = userId, taskId = saved.id)
            found.description shouldBe "Updated"
            found.status shouldBe TaskStatus.IN_PROGRESS
        }

        test("deleteTask should remove the task") {
            val saved = taskRepository.insertTask(Task(id = 0, description = "To delete", status = TaskStatus.NOT_STARTED, createdBy = userId))

            taskRepository.deleteTask(saved.id)

            taskRepository.findTasks(userId = userId).shouldBeEmpty()
        }
    }
}
