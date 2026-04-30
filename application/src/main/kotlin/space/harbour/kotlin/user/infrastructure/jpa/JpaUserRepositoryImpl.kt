package space.harbour.kotlin.user.infrastructure.jpa

import org.springframework.stereotype.Repository
import space.harbour.kotlin.user.domain.User
import space.harbour.kotlin.user.domain.UserRepository

@Repository
class JpaUserRepositoryImpl(
    private val springDataRepo: SpringDataUserRepository
) : UserRepository {

    override fun findByUsername(username: String): User? {
        return springDataRepo.findByUsername(username)?.toUser()
    }

    private fun JpaUserEntity.toUser() = User(
        id = requireNotNull(this.id),
        username = this.username,
        password = this.password,
        role = this.role,
    )
}
