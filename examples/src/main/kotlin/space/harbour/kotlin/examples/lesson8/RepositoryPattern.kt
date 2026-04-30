package space.harbour.kotlin.examples.lesson8

import space.harbour.kotlin.examples.lesson4.User

interface UserRepository {
    fun findById(id: Int): User?
    fun findAll(): List<User>
    fun save(user: User)
    fun delete(id: Int)
}

class InMemoryUserRepository : UserRepository {
    private val users = mutableMapOf<Int, User>()
    private var nextId = 1

    override fun findById(id: Int): User? = users[id]

    override fun findAll(): List<User> = users.values.toList()

    override fun save(user: User) {
        val id = if (user.id == 0) nextId++ else user.id
        users[id] = user.copy(id = id)
    }

    override fun delete(id: Int) {
        users.remove(id)
    }
}

class DatabaseUserRepository : UserRepository {
    private val database = UserDatabase

    override fun findById(id: Int) = database.get(id)
    override fun findAll(): List<User> = database.getAll()
    override fun save(user: User) {
        TODO("Not implemented")
    }
    override fun delete(id: Int) {
        TODO("Not implemented")
    }
}

fun main() {
    val repo: UserRepository = DatabaseUserRepository()

    repo.save(User(0, "Alice", "alice@example.com"))
    repo.save(User(0, "Bob", "bob@example.com"))

    println("All users:")
    repo.findAll().forEach { println(it) }

    println("\nFind user 1:")
    println(repo.findById(1))
}


object UserDatabase {
    private val users = mutableListOf<User>()

    fun get(id: Int): User = users[id]

    fun getAll(): List<User> = users.toList()

}
