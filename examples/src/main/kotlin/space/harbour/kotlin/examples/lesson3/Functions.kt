package space.harbour.kotlin.examples.lesson3

val cutApplesInHalf: (Int) -> Int = { it * 2 }

val addApplesAndOranges: (Int, Int) -> Int = { apples, oranges ->
    val result = apples + oranges

    result
}

fun getCalculator(): (Int, Long, Double) -> Double {
    return { a, b, c -> a + b + c }
}

fun main() {
    val reverseMyName: (String) -> Unit = {
        val result = it.split(" ").reversed().joinToString(", ")
        println(result)
    }

    val names = listOf("Monika Protivova", "John Doe", "Jane Doe")

    names.forEach { name ->
        reverseMyName(name)
    }

    val calculator = getCalculator()

    val result = calculator(1, 2, 3.14)

    println(result)
}
