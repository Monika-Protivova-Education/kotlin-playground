package space.harbour.kotlin.examples.lesson4

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

// Approach 1: Manual resource management with try/finally
fun readUsersManual(filePath: String): List<String> {
    var reader: BufferedReader? = null
    return try {
        reader = BufferedReader(FileReader(filePath))
        reader.readLines().filter { it.isNotBlank() }
    } catch (e: Exception) {
        println("Error: ${e.message}")
        emptyList()
    } finally {
        reader?.close()
        println("(finally: reader closed)")
    }
}

// Approach 2: Kotlin's .use {} — auto-closes the resource
fun readUsersUse(filePath: String): List<String> {
    return try {
        BufferedReader(FileReader(filePath)).use { reader ->
            reader.readLines().filter { it.isNotBlank() }
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
        emptyList()
    }
}

fun main() {
    val path = "/tmp/users.txt"
    File(path).writeText(
        """
        1,Alice,alice@mail.com
        2,Bob,bob@mail.com
        3,Charlie,charlie@mail.com
        """.trimIndent(),
    )

    println("=== try/finally ===")
    readUsersManual(path).forEach { println("  $it") }

    println("\n=== .use {} ===")
    readUsersUse(path).forEach { println("  $it") }

    println("\n=== Missing file ===")
    readUsersManual("/tmp/nonexistent.txt")
    readUsersUse("/tmp/nonexistent.txt")

    File(path).delete()
}
