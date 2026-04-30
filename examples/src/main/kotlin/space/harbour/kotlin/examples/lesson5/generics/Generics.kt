package space.harbour.kotlin.examples.lesson5.generics

open class Item(
    val value: String,
)

class StringItem(value: String) : Item(value) {
    override fun toString(): String = value.toString()
}

data class Box<T1 : Item, T2 : Number>(val item1: T1, val item2: T2)

fun <T1 : Item, T2 : Number> openTheBox1(box: Box<T1, T2>) {
    println("${box.item1} is s ${box.item1.javaClass}")
}

fun <T1 : Item, T2 : Number> Box<T1, T2>.openTheBox(): Pair<T1, T2> {
    println("$item1 is s ${item1.javaClass.simpleName}")
    println("$item2 is s ${item2.javaClass.simpleName}")
    return item1 to item2
}

fun main() {
    val box = Box(StringItem("Coin"), 2.4)

    box.openTheBox().also { println(it) }
}
