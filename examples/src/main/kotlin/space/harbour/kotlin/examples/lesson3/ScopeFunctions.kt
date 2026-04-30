package space.harbour.kotlin.examples.lesson3

import java.time.LocalDateTime

data class Message(
    val message: Text,
    val time: LocalDateTime,
    val sender: String,
    var ack: Boolean = false,
)

data class Text(
    val text: String,
    val urgency: Int,
    val hash: String,
)

fun main() {
    val message =
        Message(
            Text("Hello", 1, "adsasdasd"),
            LocalDateTime.now(),
            "Anonymous",
        )

//    println(message.message.urgency)
//    println(message.message.text)
//
//    message.message.also {
//        println(it.urgency)
//        println(it.text)
//    }
//
//    with(message.message) {
//        println(urgency)
//        println(text)
//    }

    println(message)

    message.apply {
        ack = true
    }

    println(message)
}
