package space.harbour.kotlin.task.application

import space.harbour.kotlin.task.domain.TaskStatus

data class MoveTaskDTO(
    val status: TaskStatus
)
