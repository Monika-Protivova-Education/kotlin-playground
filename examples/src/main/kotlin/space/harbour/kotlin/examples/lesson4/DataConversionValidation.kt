package space.harbour.kotlin.examples.lesson4

import space.harbour.kotlin.examples.lesson4.User

fun parseUserFromCsv(line: String): User {
    val parts = line.split(",").map { it.trim() }
    require(parts.size == 3) { "Expected 3 fields, got ${parts.size}" }

    val id =
        parts[0].toIntOrNull()
            ?: throw NumberFormatException("Invalid id: '${parts[0]}'")
    val name =
        parts[1].takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Name must not be blank")
    val email =
        parts[2].takeIf { it.contains("@") }
            ?: throw IllegalArgumentException("Invalid email: '${parts[2]}'")

    return User(id, name, email)
}

fun importUsers(lines: List<String>): Pair<List<User>, List<String>> {
    val users = mutableListOf<User>()
    val errors = mutableListOf<String>()

    for (line in lines) {
        try {
            users.add(parseUserFromCsv(line))
        } catch (e: Exception) {
            errors.add("'$line' — ${e.message}")
        }
    }
    return users to errors
}

fun main() {
    val lines =
        listOf(
            "1,Alice,alice@mail.com",
            "2,Bob,bob@mail.com",
            "abc,Charlie,charlie@mail.com",
            "4,,diana@mail.com",
            "5,Eve,no-at-sign",
        )

    val (users, errors) = importUsers(lines)

    println("=== Data conversion with validation ===")
    println("Imported ${users.size}:")
    users.forEach { println("  $it") }

    println("Errors ${errors.size}:")
    errors.forEach { println("  $it") }
}
