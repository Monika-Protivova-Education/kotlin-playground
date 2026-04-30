package space.harbour.kotlin.task.domain

interface TaskRepository {
    fun findTask(userId: Long, taskId: Long): Task
    fun findTasks(
        userId: Long,
        status: TaskStatus? = null,
        startsWith: String? = null
    ): List<Task>
    fun insertTask(task: Task): Task
    fun updateTask(task: Task)
    fun deleteTask(id: Long)
}
