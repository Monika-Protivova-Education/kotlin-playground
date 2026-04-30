package space.harbour.kotlin.examples.lesson8

object DatabaseConnection {

    const val MAX_CONNECTION = 3

    private var actionConnections: MutableSet<String> = mutableSetOf()

    fun connect(id: String) {
        require(actionConnections.size < MAX_CONNECTION) { "No connections available" }
        actionConnections.add(id)
    }

    fun disconnect(id: String) {
        actionConnections.remove(id)
    }

    fun getConnectionCount() = actionConnections
}


fun main() {
    DatabaseConnection.connect("a")
    DatabaseConnection.connect("b")
    DatabaseConnection.connect("c")
    println("Count: ${DatabaseConnection.getConnectionCount()}")
    DatabaseConnection.disconnect("a")

    DatabaseConnection.connect("d")
}
