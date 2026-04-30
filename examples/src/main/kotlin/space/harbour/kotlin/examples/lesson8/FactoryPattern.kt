package space.harbour.kotlin.examples.lesson8

interface Transport {
    fun deliver(destination: String)
}

enum class TransportMode {
    TRUCK,
    SHIP,
    PLANE,
    TRAIN
}

class TruckTransport : Transport {
    override fun deliver(destination: String) =
        println("Delivering by truck to $destination")
}

class ShipTransport : Transport {
    override fun deliver(destination: String) =
        println("Delivering by ship to $destination")
}

class PlaneTransport : Transport {
    override fun deliver(destination: String) =
        println("Delivering by plane to $destination")
}

class FastTrainTransport : Transport {
    override fun deliver(destination: String) =
        println("Delivering by fast train to $destination")
}

class SlowTrainTransport : Transport {
    override fun deliver(destination: String) =
        println("Delivering by slow train to $destination")
}

object TransportFactory {

    fun createTransport(
        distance: Int,
        highPriority: Boolean
    ): Transport = when (distance) {
        in 0..200 -> {
            when (highPriority) {
                true -> SlowTrainTransport()
                else -> TruckTransport()
            }
        }
        in 201.. 2000 -> {
            when (highPriority) {
                true -> SlowTrainTransport()
                else -> FastTrainTransport()
            }
        }
        else -> {
            when (highPriority) {
                true -> FastTrainTransport()
                else -> PlaneTransport()
            }
        }

    }

}

fun main() {
    val transport1 = TransportFactory.createTransport(3000, false)
    transport1.deliver("New York")

    val transport2 = TransportFactory.createTransport(1000, true)
    transport2.deliver("London")
}
