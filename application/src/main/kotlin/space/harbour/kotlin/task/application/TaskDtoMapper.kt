package space.harbour.kotlin.task.application

import space.harbour.kotlin.task.domain.Task
import kotlin.random.Random


internal fun TaskDTO.toTask() = Task(
    id = Random.nextLong(),
    description = this.description,
    status = this.status,
    createdBy = this.createdBy,
)

internal fun UpdateTaskDTO.toTask(id: Long, createdBy: Long) = Task(
    id = id,
    description = this.description,
    status = this.status,
    createdBy = createdBy,
)

internal fun Task.toDto() = TaskDTO(
    id = this.id,
    description = this.description,
    status = this.status,
    createdBy = this.createdBy,
)
