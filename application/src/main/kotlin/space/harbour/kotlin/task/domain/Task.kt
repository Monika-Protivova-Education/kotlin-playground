package space.harbour.kotlin.task.domain

data class Task(
    val id: Long,
    val description: String,
    val status: TaskStatus = TaskStatus.NOT_STARTED,
    val createdBy: Long
)
