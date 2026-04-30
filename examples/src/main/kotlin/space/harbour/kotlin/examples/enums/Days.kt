package space.harbour.kotlin.examples.enums

internal enum class Days(val index: Int, val vibe: String) {
    MONDAY(1, "Bad"),
    TUESDAY(2, "Still bad"),
    WEDNESDAY(3, "Ugh"),
    THURSDAY(4, "Getting better"),
    FRIDAY(5, "Yay"),
    SATURDAY(6, "Good"),
    SUNDAY(7, "Zzz"),
}

internal fun space.harbour.kotlin.examples.enums.Days.getVibe(): space.harbour.kotlin.examples.enums.Days {
    println("$name has $vibe vibe")
    return this
}

internal fun space.harbour.kotlin.examples.enums.Days.getNext(): space.harbour.kotlin.examples.enums.Days {
    return Days.entries[(this.ordinal + 1) % 7]
}

fun main() {
//    Days.FRIDAY
//        .getVibe()
//        .getNext()
//        .getVibe()
//        .getNext()
//        .getVibe()

    val result =
        listOf("Luke Skywalker", "Darth Vader").mapIndexed { index, character ->
            character to index
        }

    result.forEach { println(it) }
}
