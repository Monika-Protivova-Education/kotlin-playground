package space.harbour.kotlin.task.infrastructure.jpa

import org.springframework.context.annotation.Profile
import space.harbour.kotlin.Profiles
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository
import space.harbour.kotlin.error.AppError
import space.harbour.kotlin.task.domain.Task
import space.harbour.kotlin.task.domain.TaskRepository
import space.harbour.kotlin.task.domain.TaskStatus

@Repository
@Profile(Profiles.JPA)
class JpaTaskRepositoryImpl(
    private val springDataRepo: SpringDataTaskRepository
) : TaskRepository {

    override fun findTask(userId: Long, taskId: Long): Task {
        val spec = Specification
            .where(TaskSpecifications.createdBy(userId))
            .and(TaskSpecifications.id(taskId))

        return springDataRepo.findAll(spec).map { it.toTask() }.firstOrNull()
            ?: throw AppError.TaskNotFound(taskId ?: 0)
    }

    override fun findTasks(
        userId: Long,
        status: TaskStatus?,
        startsWith: String?
    ): List<Task> {
        val spec = Specification
            .where(TaskSpecifications.createdBy(userId))
            .and(TaskSpecifications.status(status))
            .and(TaskSpecifications.startsWith(startsWith))

        return springDataRepo.findAll(spec).map { it.toTask() }
    }

    override fun insertTask(task: Task): Task {
        val saved = springDataRepo.save(task.toEntity())
        return saved.toTask()
    }

    override fun updateTask(task: Task) {
        springDataRepo.save(task.toEntity())
    }

    override fun deleteTask(id: Long) {
        springDataRepo.deleteById(id)
    }

    private fun JpaTaskEntity.toTask() = Task(
        id = requireNotNull(this.id),
        createdBy = this.createdBy,
        description = this.description,
        status = this.status,
    )

    private fun Task.toEntity() = JpaTaskEntity(
        id = if (this.id == 0L) null else this.id,
        createdBy = this.createdBy,
        description = this.description,
        status = this.status,
    )
}


object TaskSpecifications {

    fun id(id: Long?): Specification<JpaTaskEntity> {
        return Specification { root, _, cb ->
            id?.let { cb.equal(root.get<Long>("id"), it) }
        }
    }

    fun createdBy(userId: Long): Specification<JpaTaskEntity> {
        return Specification { root, _, cb ->
            cb.equal(root.get<Long>("createdBy"), userId)
        }
    }

    fun status(status: TaskStatus?): Specification<JpaTaskEntity> {
        return Specification { root, _, cb ->
            status?.let { cb.like(root.get("status"), it.name) }
        }
    }

    fun startsWith(startsWith: String?): Specification<JpaTaskEntity> {
        return Specification { root, _, cb ->
            startsWith?.let { cb.like(root.get("description"), "$it%") }
        }
    }
}
