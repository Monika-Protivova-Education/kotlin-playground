package space.harbour.kotlin.examples.lesson6

import kotlin.math.roundToLong

enum class DogStatus {
    AVAILABLE,
    RESERVED,
    ADOPTED,
}

data class Dog(
    val name: String,
    val ageYears: Int,
    val isVaccinated: Boolean,
    var status: DogStatus = DogStatus.AVAILABLE,
)

data class DogListing(
    val name: String,
    val ageYears: Int,
    val isVaccinated: Boolean,
    val status: DogStatus,
    val adoptionFee: Double,
)

data class AdoptionRecord(
    val dogName: String,
    val adopterName: String,
    val feePaid: Double,
)

class DogShelter {
    private val dogs = mutableMapOf<String, Dog>()
    private val registry = mutableListOf<AdoptionRecord>()

    fun register(
        name: String,
        ageYears: Int,
        isVaccinated: Boolean,
    ): Dog {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(ageYears in 0..30) { "Age must be between 0 and 30" }
        require(name !in dogs) { "Dog with name $name already exists" }

        val dog = Dog(name, ageYears, isVaccinated)
        dogs[name] = dog
        return dog
    }

    fun list(): List<DogListing> {
        return dogs.values.map { dog ->
            DogListing(
                name = dog.name,
                ageYears = dog.ageYears,
                isVaccinated = dog.isVaccinated,
                status = dog.status,
                adoptionFee = calculateFee(dog),
            )
        }
    }

    fun reserve(name: String): Dog {
        val dog = dogs[name] ?: throw IllegalArgumentException("Dog $name not found")
        require(dog.status == DogStatus.AVAILABLE) { "Cannot reserve dog in ${dog.status} state" }
        dog.status = DogStatus.RESERVED
        return dog
    }

    fun adopt(dogName: String, adopterName: String): Dog {
        require(adopterName.isNotBlank()) { "Adopter name must not be blank" }
        val dog = dogs[dogName] ?: throw IllegalArgumentException("Dog $dogName not found")
        require(dog.status == DogStatus.RESERVED) { "Cannot adopt dog in ${dog.status} state" }
        dog.status = DogStatus.ADOPTED
        registry.add(AdoptionRecord(dogName, adopterName, calculateFee(dog)))
        return dog
    }

    fun adoptions(): List<AdoptionRecord> = registry.toList()

    private fun calculateFee(dog: Dog): Double {
        var fee = 150.0

        if (dog.ageYears >= 8) fee *= 0.5

        if (dog.isVaccinated) fee -= 20.0

        return maxOf(0.0, (fee * 100).roundToLong() / 100.0)
    }
}
