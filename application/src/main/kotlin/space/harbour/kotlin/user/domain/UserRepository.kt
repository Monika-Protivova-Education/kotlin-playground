package space.harbour.kotlin.user.domain

interface UserRepository {
    fun findByUsername(username: String): User?
}
