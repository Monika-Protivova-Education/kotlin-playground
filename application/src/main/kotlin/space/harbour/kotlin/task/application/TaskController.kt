package space.harbour.kotlin.task.application

import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import space.harbour.kotlin.task.domain.Task
import space.harbour.kotlin.task.domain.TaskService
import space.harbour.kotlin.task.domain.TaskStatus

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService,
) {
    @GetMapping
    fun getTasks(
        @RequestParam(name = "status", required = false) status: TaskStatus?,
        @RequestParam(name = "search", required = false) startsWith: String?,
    ): List<TaskDTO> {
        return taskService.getTasks(
            status = status,
            startsWith = startsWith
        ).map { it.toDto() }
    }

    @GetMapping("/{id}")
    fun getTask(
        @PathVariable id: Long
    ): Task = taskService.getTask(id = id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addTask(@RequestBody task: TaskDTO): Task {
        return taskService.addTask(task.toTask())
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @RequestBody task: UpdateTaskDTO
    ) {
        val existingTask = taskService.getTask(id)
        taskService.updateTask(id = id, task = task.toTask(id, existingTask.createdBy))
    }

    @PatchMapping("/{id}/status")
    fun moveTask(
        @PathVariable id: Long,
        @RequestBody body: MoveTaskDTO
    ): Task {
        return taskService.moveTask(id = id, newStatus = body.status)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTask(@PathVariable id: Long) {
        taskService.deleteTask(id)
    }
}
