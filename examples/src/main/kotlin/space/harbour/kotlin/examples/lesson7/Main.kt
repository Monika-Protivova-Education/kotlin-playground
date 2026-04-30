package space.harbour.kotlin.examples.lesson7

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    // StateFlow — holds current state, emits to new collectors
    val stateFlow = MutableStateFlow(0)

    val job = launch {
        stateFlow.collect { println("State: $it") }
    }

    stateFlow.value = 1
    delay(50)
    stateFlow.value = 2
    delay(50)
    stateFlow.value = 2 // NOT emitted (same value, distinctUntilChanged)
    stateFlow.value = 3
    delay(50)

    job.cancel()
}
