package space.harbour.kotlin.task.application

import space.harbour.kotlin.task.domain.TaskStatus

data class UpdateTaskDTO(
    val description: String,
    val status: TaskStatus
)
