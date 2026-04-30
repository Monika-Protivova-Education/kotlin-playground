package space.harbour.kotlin.examples.lesson4

import java.nio.file.Paths
import kotlin.io.path.readLines

fun main() {
    val inputPath = Paths.get("/Users/monikaprotivova/IdeaProjects/Education/kotlin-playground/src/main/resources/users.csv")

    inputPath.readLines().drop(1).mapNotNull { line ->

        val line = line.split(",").map { it.trim() }
        val (id, name, email) = line

        try {
            UserData(
                id = requireNotNull(id.toLongOrNull()),
                name = name,
                email = email,
                role = UserRole.USER,
            )
        } catch (e: RuntimeException) {
            println("ERROR: Invalid user data - $line")
            null
        }
    }.forEach(::println)
}
