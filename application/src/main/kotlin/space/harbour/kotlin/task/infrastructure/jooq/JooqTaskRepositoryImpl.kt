package space.harbour.kotlin.task.infrastructure.jooq

import org.jooq.DSLContext
import org.springframework.context.annotation.Profile
import space.harbour.kotlin.Profiles
import org.springframework.stereotype.Repository
import space.harbour.kotlin.error.AppError
import space.harbour.kotlin.jooq.generated.tables.references.TASKS
import space.harbour.kotlin.task.domain.Task
import space.harbour.kotlin.task.domain.TaskRepository
import space.harbour.kotlin.task.domain.TaskStatus

@Repository
@Profile(Profiles.JOOQ)
class JooqTaskRepositoryImpl(
    private val dsl: DSLContext
) : TaskRepository {

    override fun findTask(userId: Long, taskId: Long): Task {
        val conditions = listOfNotNull(
            TASKS.CREATED_BY.eq(userId),
            taskId?.let { TASKS.ID.eq(it) },
        )
        return dsl.selectFrom(TASKS)
            .where(conditions)
            .fetchInto(Task::class.java)
            .firstOrNull()
            ?: throw AppError.TaskNotFound(taskId ?: 0)
    }

    override fun findTasks(userId: Long, status: TaskStatus?, startsWith: String?): List<Task> {
        return dsl.selectFrom(TASKS)
            .where(
                listOfNotNull(
                    TASKS.CREATED_BY.eq(userId),
                    status?.let { TASKS.STATUS.eq(it.name) },
                    startsWith?.let { TASKS.DESCRIPTION.like("$it%") },
                )
            )
            .fetchInto(Task::class.java)
    }

    override fun insertTask(task: Task): Task {
        dsl.insertInto(TASKS)
            .set(TASKS.ID, task.id)
            .set(TASKS.STATUS, task.status.name)
            .set(TASKS.DESCRIPTION, task.description)
            .set(TASKS.CREATED_BY, task.createdBy)
            .execute()

        return task
    }

    override fun updateTask(task: Task) {
        dsl.update(TASKS)
            .set(TASKS.STATUS, task.status.name)
            .set(TASKS.DESCRIPTION, task.description)
            .where(TASKS.ID.eq(task.id))
            .execute()
    }

    override fun deleteTask(id: Long) {
        dsl.deleteFrom(TASKS)
            .where(TASKS.ID.eq(id))
            .execute()
    }
}
