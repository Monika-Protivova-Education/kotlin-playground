package space.harbour.kotlin.task.infrastructure.jdbc

import org.springframework.context.annotation.Profile
import space.harbour.kotlin.Profiles
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import space.harbour.kotlin.error.AppError
import space.harbour.kotlin.task.domain.Task
import space.harbour.kotlin.task.domain.TaskRepository
import space.harbour.kotlin.task.domain.TaskStatus
import java.sql.Statement

@Repository
@Profile(Profiles.JDBC)
class JdbcTaskRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : TaskRepository {

    private val rowMapper = RowMapper { rs, _ ->
        Task(
            id = rs.getLong("id"),
            description = rs.getString("description"),
            status = TaskStatus.valueOf(rs.getString("status")),
            createdBy = rs.getLong("created_by")
        )
    }

    override fun findTask(userId: Long, taskId: Long): Task {
        return jdbcTemplate.query(
            "SELECT id, description, status, created_by FROM tasks WHERE created_by = ? AND id = ?",
            rowMapper,
            userId,
            taskId
        ).firstOrNull()
            ?: throw AppError.TaskNotFound(taskId)
    }

    override fun findTasks(userId: Long, status: TaskStatus?, startsWith: String?): List<Task> {
        val conditions = mutableListOf("created_by = ?")
        val params = mutableListOf<Any>(userId)

        if (status != null) {
            conditions.add("status = ?")
            params.add(status.name)
        }
        if (startsWith != null) {
            conditions.add("description LIKE ?")
            params.add("$startsWith%")
        }

        val where = " WHERE " + conditions.joinToString(" AND ")
        val sql = "SELECT id, description, status, created_by FROM tasks$where"

        return jdbcTemplate.query(sql, rowMapper, *params.toTypedArray())
    }

    override fun insertTask(task: Task): Task {
        val keyHolder = GeneratedKeyHolder()
        jdbcTemplate.update({ connection ->
            val ps = connection.prepareStatement(
                "INSERT INTO tasks (description, status, created_by) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            )
            ps.setString(1, task.description)
            ps.setString(2, task.status.name)
            ps.setLong(3, task.createdBy)
            ps
        }, keyHolder)

        val generatedId = keyHolder.keys?.get("id") as Long
        return task.copy(id = generatedId)
    }

    override fun updateTask(task: Task) {
        jdbcTemplate.update(
            "UPDATE tasks SET description = ?, status = ?, created_by = ? WHERE id = ?",
            task.description, task.status.name, task.createdBy, task.id
        )
    }

    override fun deleteTask(id: Long) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id)
    }
}
