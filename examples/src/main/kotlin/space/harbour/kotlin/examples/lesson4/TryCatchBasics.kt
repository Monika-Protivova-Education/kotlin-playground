package space.harbour.kotlin.examples.lesson4

fun parseUser(record: String): User {
    val parts = record.split(",")
    val id = parts[0].toInt()
    val name = parts[1]
    val email = parts[2]
    return User(id, name, email)
}

fun main() {
    val input1 = "1,Alice,alice@mail.com"
    val input2 = "bad,Bob,bob@mail.com"
    val input3 = "3"
}
