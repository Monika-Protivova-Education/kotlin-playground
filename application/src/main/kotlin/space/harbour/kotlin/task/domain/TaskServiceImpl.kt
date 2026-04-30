package space.harbour.kotlin.task.domain

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import space.harbour.kotlin.error.invalidTaskStatus
import space.harbour.kotlin.user.domain.UserService

@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val userService: UserService,
) : TaskService {

    override fun getTask(id: Long): Task {
        val userId = userService.getCurrentUserIdOrThrow()
        return taskRepository.findTask(userId = userId, taskId = id)
    }

    override fun getTasks(
        status: TaskStatus?,
        startsWith: String?
    ): List<Task> {
        val userId = userService.getCurrentUserIdOrThrow()
        return taskRepository.findTasks(
            userId = userId,
            status = status,
            startsWith = startsWith
        )
    }

    override fun addTask(task: Task): Task {
        val userId = userService.getCurrentUserIdOrThrow()
        return taskRepository.insertTask(task.copy(createdBy = userId))
    }

    @Transactional
    override fun updateTask(id: Long, task: Task) {
        val userId = userService.getCurrentUserIdOrThrow()
        val existingTask = taskRepository.findTask(userId = userId, taskId = id)

        when (existingTask.status) {
            TaskStatus.NOT_STARTED,
            TaskStatus.IN_PROGRESS -> taskRepository.updateTask(task)
            TaskStatus.COMPLETED -> invalidTaskStatus(id, existingTask.status, task.status)
        }
    }

    override fun moveTask(id: Long, newStatus: TaskStatus): Task {
        val userId = userService.getCurrentUserIdOrThrow()
        val existingTask = taskRepository.findTask(userId = userId, taskId = id)

        if (existingTask.status == TaskStatus.COMPLETED) {
            invalidTaskStatus(id, existingTask.status, newStatus)
        }

        val updatedTask = existingTask.copy(status = newStatus)
        taskRepository.updateTask(updatedTask)
        return updatedTask
    }

    override fun deleteTask(id: Long) {
        val userId = userService.getCurrentUserIdOrThrow()
        taskRepository.findTask(userId = userId, taskId = id)
        taskRepository.deleteTask(id)
    }
}
