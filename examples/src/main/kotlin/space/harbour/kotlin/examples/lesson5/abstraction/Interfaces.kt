package space.harbour.kotlin.examples.lesson5.abstraction

data class Sound(private val sound: String) {
    fun make() {
        println(sound)
    }

    fun getSound(): String = sound
}

enum class Movement(val value: String) {
    WALK("walks"),
    FLY("flies"),
    SWIM("swims"),
    SLITHER("slithers"),
    HOP("hops"),
}

interface Vocalizing {
    fun makeSound()
}

interface Mobile {
    fun move()
}

interface Flying : Mobile {
    fun fly()
}

interface Swimming : Mobile {
    fun swim()
}

interface Walking : Mobile {
    fun walk()
}

abstract class Animal(
    val name: String,
    val sound: Sound,
    val movement: Movement,
) : Vocalizing {
    fun move() {
        println("$name ${movement.value}")
    }

    override fun makeSound() {
        sound.make()
    }

    abstract fun makeSecondSound()
}

class Dog(name: String) : Animal(name, Sound("woof"), Movement.WALK) {
    override fun makeSecondSound() {
        println("${sound.getSound()} ... yelp ... ${sound.getSound()}")
    }
}

class Cat(name: String) : Animal(name, Sound("meow"), Movement.WALK) {
    override fun makeSecondSound() {
        println("${sound.getSound()} ... hisss ... ${sound.getSound()}")
    }
}

class Bird(name: String) : Animal(name, Sound("tweet"), Movement.FLY) {
    override fun makeSecondSound() {
        super.makeSound()
    }
}

object Snake : Animal("", Sound(""), Movement.SLITHER) {
    override fun makeSecondSound() {
        TODO("Not yet implemented")
    }
}

open class Fish(name: String) : Animal(name, Sound("..."), Movement.SWIM) {
    override fun makeSecondSound() {
        super.makeSound()
    }
}

class Betta(name: String) : Fish(name) {
    override fun makeSecondSound() {
        super.makeSound()
    }
}

fun main() {
    val petStore =
        listOf(
            Dog("Benny"),
            Cat("Oli"),
            Bird("Joe"),
            Betta("Bento"),
        )

    petStore.forEach { animal ->
        animal.makeSound()
        animal.makeSecondSound()
        animal.move()
    }
}
