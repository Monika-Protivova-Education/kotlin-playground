package space.harbour.kotlin.examples.lesson8

interface Engine {
    fun on()
    fun off()
    fun drive(km: Int): Int
    fun remainingRange(): Int
}

class ElectricEngine(range: Int) : Engine {
    private var remainingRange = range

    override fun on() {
        println("Electric engine is on")
    }

    override fun off() {
        println("Electric engine is off")
    }

    override fun drive(km: Int): Int {
        remainingRange -= km
        return remainingRange
    }
    override fun remainingRange() = remainingRange
}

class GasolineEngine(range: Int) : Engine {
    private var remainingRange = range

    override fun on() {
        println("Gasoline engine is on")
    }

    override fun off() {
        println("Gasoline engine is off")
    }

    override fun drive(km: Int): Int {
        remainingRange -= km
        return remainingRange
    }
    override fun remainingRange() = remainingRange
}

class Car(private val engine: Engine) {

    fun drive(km: Int): Int {
        engine.on()
        engine.drive(km)
        engine.off()
        return engine.remainingRange()
    }

}


fun main() {
    val car1 = Car(
        engine = ElectricEngine(200),
    )
    val car2 = Car(
        engine = GasolineEngine(200),
    )

    val range = car2.drive(50)
    println("Car has $range km left")
}
