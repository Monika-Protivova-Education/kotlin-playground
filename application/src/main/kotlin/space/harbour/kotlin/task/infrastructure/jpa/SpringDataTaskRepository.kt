package space.harbour.kotlin.task.infrastructure.jpa

import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import space.harbour.kotlin.task.domain.Task
import space.harbour.kotlin.task.domain.TaskStatus

interface SpringDataTaskRepository : JpaRepository<JpaTaskEntity, Long> {

    @Query("select t from JpaTaskEntity t where t.status = :status")
    fun findAllByStatus(@Param("status") taskStaus: TaskStatus): List<JpaTaskEntity>

    fun findAll(specification: Specification<JpaTaskEntity>): List<JpaTaskEntity>


}
