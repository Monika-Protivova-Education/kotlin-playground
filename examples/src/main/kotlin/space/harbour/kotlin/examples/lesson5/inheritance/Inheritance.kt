// package space.harbour.kotlin.lesson5.inheritance
//
// private open class Animal(val sound: String = "...") {
//    open fun makeSound(): String {
//        return sound
//    }
//
//    protected open fun checkHealth() {
//
//    }
//
//    private fun convertToImage() {
//
//    }
// }
//
// private open class Pet {
//
// }
//
// private class space.harbour.kotlin.lesson6.Dog : Animal("Woof") {
//
//    fun run(timeMillis: Int, speedMs: Int): Int {
//        return timeMillis * speedMs
//    }
//
//    override fun makeSound(): String {
//        return sound
//    }
//
//    override fun checkHealth() {
//
//    }
// }
//
// private class Bird(song: String) : Animal(song)
//
// private class Cat: Animal {
//
//    var sound2: String = ""
//        private set
//
//    constructor(): super("Meow")
//
//    constructor(sound1: String, sound2: String): super(sound1) {
//        this.sound2 = sound2
//    }
// }
//
// private class PetStore {
//
//    fun buyAnimal(): Animal {
//        return Cat("Meow", "Hiss")
//    }
//
// }
//
// fun main() {
//    val petStore = PetStore()
//
//    val animal: Animal = petStore.buyAnimal()
//
//    when (animal) {
//        is space.harbour.kotlin.lesson6.Dog -> {
//            val dogSound = animal.makeSound()
//            val distance = animal.run(1000, 100)
//
//            println(dogSound)
//            println(distance)
//        }
//        is Cat -> {
//            println(animal.sound)
//            println(animal.sound2)
//        }
//        else -> {}
//    }
//
//
//
//
//
// }
