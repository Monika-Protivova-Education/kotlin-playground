package space.harbour.kotlin.task.application

import space.harbour.kotlin.task.domain.TaskStatus

data class TaskDTO(
    val id: Long,
    val description: String,
    val status: TaskStatus = TaskStatus.NOT_STARTED,
    val createdBy: Long
)
