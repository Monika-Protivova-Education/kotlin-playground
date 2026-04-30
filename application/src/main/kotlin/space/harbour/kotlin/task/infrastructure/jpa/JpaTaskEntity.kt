package space.harbour.kotlin.task.infrastructure.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import space.harbour.kotlin.task.domain.TaskStatus

@Entity
@Table(name = "tasks")
data class JpaTaskEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val createdBy: Long,

    val description: String,

    @Enumerated(EnumType.STRING)
    val status: TaskStatus,
)
