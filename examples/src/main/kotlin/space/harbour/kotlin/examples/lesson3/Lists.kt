package space.harbour.kotlin.examples.lesson3

fun main() {
    val numbers = (1..1000000).toList()

// Collection approach (eager evaluation)
    val resultCollection =
        numbers
            .filter { it % 2 == 0 } // Creates intermediate list
            .map {
                Thread.sleep(5)
                it * it
            } // Creates another intermediate list
            .take(5) // Creates final list with 5 elements

// Sequence approach (lazy evaluation)
    val resultSequence =
        numbers.asSequence()
            .filter { it % 2 == 0 } // No intermediate collection created
            .map {
                Thread.sleep(5)
                it * it
            } // No intermediate collection created
            .take(5) // Only processes first 10 elements to get 5 results
            .toList() // Terminal operation - converts to list
}
