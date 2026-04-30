package space.harbour.kotlin.user.domain

data class User(
    val id: Long,
    val username: String,
    val password: String,
    val role: UserRole,
)

enum class UserRole {
    USER,
    ADMIN,
}
