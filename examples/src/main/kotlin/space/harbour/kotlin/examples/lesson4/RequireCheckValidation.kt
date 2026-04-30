package space.harbour.kotlin.examples.lesson4

// Database data

data class UserRecord(
    val id: Long? = null,
    val name: String? = null,
    val email: String? = null,
    val role: String? = null,
)

// Application model

enum class UserRole {
    USER,
    ADMIN,
}

data class UserData(
    val id: Long,
    val name: String,
    val email: String? = null,
    val role: UserRole,
)

sealed class AppException(message: String) : RuntimeException(message) {
    class InvalidRoleException(
        val user: String,
        val role: UserRole,
    ) : RuntimeException("Invalid role: $role")

    class GeneralFailureException(
        massage: String,
    ) : RuntimeException(massage)
}

fun UserData.validateAmin(): UserData {
    if (this.role != UserRole.ADMIN) {
        throw AppException.InvalidRoleException(this.name, this.role)
    }

    listOf(true, false).random().let {
        if (it) throw AppException.GeneralFailureException("Something went wrong")
    }

    return this
}

fun main() {
    val userRecord =
        UserRecord(
            id = 1,
            name = "Monika",
            email = null,
            role = listOf("ADMIN", "USER").random(),
        )

    val user =
        UserData(
            id = requireNotNull(userRecord.id) { "User ID must not be null" },
            name = requireNotNull(userRecord.name) { "User name must not be null" },
            email = userRecord.email,
            role =
                requireNotNull(userRecord.role) { "User role must not be null " }
                    .let { UserRole.valueOf(it) },
        )

    try {
        user.validateAmin()
    } catch (e: Exception) {
        when (e) {
            is AppException.GeneralFailureException -> {
                println("FATAL: ${e.message}")
                throw e
            }
            is AppException.InvalidRoleException -> {
                println("ERROR: ${e.user}/${e.role} - ${e.message}")
                throw e
            }
            else -> {
                println("FATAL: Something unexpected happend")
                throw e
            }
        }
    } finally {
        // some action to do as a cleanup
    }

    println(user)
}
