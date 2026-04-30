package space.harbour.kotlin.task.domain

interface TaskService {
    fun getTask(id: Long): Task
    fun getTasks(
        status: TaskStatus? = null,
        startsWith: String? = null
    ): List<Task>
    fun addTask(task: Task): Task
    fun updateTask(id: Long, task: Task)
    fun moveTask(id: Long, newStatus: TaskStatus): Task
    fun deleteTask(id: Long)
}
