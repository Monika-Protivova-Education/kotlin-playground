package space.harbour.kotlin.error

import space.harbour.kotlin.task.domain.TaskStatus

sealed class AppError(message: String): Exception(message) {
    data class TaskNotFound(val id: Long) : AppError(message = "Task not found: $id")
    data class InvalidTaskStatus(
        val id: Long,
        val status1: TaskStatus,
        val status2: TaskStatus
    ) : AppError(message = "Task $id is in $status1 and cannot be transferred to $status2")
    data class Forbidden(val id: Long) : AppError(message = "You do not have permission to modify task $id")
    data class Unauthorized(val reason: String = "Unauthorized") : AppError(message = reason)
}

fun taskNotFound(id: Long) {
    throw AppError.TaskNotFound(id)
}

fun invalidTaskStatus(id: Long, status1: TaskStatus, status2: TaskStatus) {
    throw AppError.InvalidTaskStatus(id, status1, status2)
}
