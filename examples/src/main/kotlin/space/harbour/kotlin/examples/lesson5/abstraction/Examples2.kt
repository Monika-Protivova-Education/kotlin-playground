package space.harbour.kotlin.examples.lesson5.abstraction

interface Vocalizing2 {
    fun makeSound()
}

interface Flying2 {
    fun fly()
}

interface Runnig {
    fun move() {
        println("Run")
    }
}

interface Walking2 {
    fun move() {
        println("Walk")
    }
}

abstract class Animal2 : Vocalizing2 {
    open fun move() {
        println("")
    }
}

class Dog2 : Animal2(), Runnig, Walking2 {
    override fun makeSound() {
        TODO("Not yet implemented")
    }

    override fun move() {
        super<Runnig>.move()
    }
}

class Fish2 : Animal2(), Swimming, Flying2 {
    override fun makeSound() {
        TODO("Not yet implemented")
    }

    override fun swim() {
        TODO("Not yet implemented")
    }

    override fun move() {
        TODO("Not yet implemented")
    }

    override fun fly() {
        TODO("Not yet implemented")
    }
}

fun main() {
    val animal = Dog2()

    animal.move()
}
