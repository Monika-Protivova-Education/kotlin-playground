package space.harbour.kotlin.examples.lesson5

object Calculator {
    // method with 2 parameters
    fun add(
        a: Int,
        b: Int,
    ): Int {
        return a + b
    }

    // overloaded method with 3 parameters
    fun add(
        a: Int,
        b: Int,
        c: Int,
    ): Int {
        return a + b + c
    }

    fun add(
        a: Double,
        b: Double,
    ): Int {
        return a.toInt() + b.toInt()
    }
}

fun main() {
    val result1 = Calculator.add(10, 20)
    println(result1)

    val result2 = Calculator.add(10, 20, 30)
    println(result2)
}
