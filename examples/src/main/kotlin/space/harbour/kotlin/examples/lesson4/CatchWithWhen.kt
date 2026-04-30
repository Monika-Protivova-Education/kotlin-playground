package space.harbour.kotlin.examples.lesson4

import space.harbour.kotlin.examples.lesson4.User

class AuthException(message: String) : Exception(message)

class ValidationException(val field: String, message: String) : Exception(message)

fun getUser(
    token: String?,
    userId: Int,
): User {
    if (token == null) throw AuthException("No token provided")
    if (userId <= 0) throw ValidationException("userId", "Must be positive")
    if (userId == 99) throw RuntimeException("Database error")

    return User(userId, "User$userId", "user$userId@mail.com")
}

fun safeGetUser(
    token: String?,
    userId: Int,
): String {
    return try {
        getUser(token, userId).toString()
    } catch (e: Exception) {
        when (e) {
            is AuthException -> "AUTH: ${e.message}"
            is ValidationException -> "INVALID ${e.field}: ${e.message}"
            else -> "UNEXPECTED: ${e.message}"
        }
    }
}

fun main() {
    println("=== Catch with when ===")
    println(safeGetUser("valid", 1))
    println(safeGetUser(null, 1))
    println(safeGetUser("valid", -5))
    println(safeGetUser("valid", 99))
}
