package space.harbour.kotlin.examples.lesson8

interface SortingStrategy {
    fun sort(data: MutableList<Int>)
}

class BubbleSort : SortingStrategy {
    override fun sort(data: MutableList<Int>) {
        println("Sorting using Bubble Sort")
        // Simplified bubble sort
        for (i in 0 until data.size) {
            for (j in 0 until data.size - 1 - i) {
                if (data[j] > data[j + 1]) {
                    val temp = data[j]
                    data[j] = data[j + 1]
                    data[j + 1] = temp
                }
            }
        }
    }
}

class QuickSort : SortingStrategy {
    override fun sort(data: MutableList<Int>) {
        println("Sorting using Quick Sort")
        data.sort() // Using built-in sort
    }
}

class DataProcessor(private var strategy: SortingStrategy) {
    fun setStrategy(strategy: SortingStrategy) {
        this.strategy = strategy
    }

    fun process(data: MutableList<Int>) {
        strategy.sort(data)
        println("Result: $data")
    }
}

fun main() {
    val processor = DataProcessor(BubbleSort())
    processor.process(mutableListOf(5, 2, 8, 1, 9))

    val processor2 = DataProcessor(QuickSort())
    processor2.process(mutableListOf(7, 3, 6, 4, 2))
}
