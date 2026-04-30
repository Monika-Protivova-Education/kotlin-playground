package space.harbour.kotlin.user.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataUserRepository : JpaRepository<JpaUserEntity, Long> {
    fun findByUsername(username: String): JpaUserEntity?
}
