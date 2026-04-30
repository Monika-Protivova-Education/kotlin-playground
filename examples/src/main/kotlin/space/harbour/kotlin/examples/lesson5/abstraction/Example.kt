package space.harbour.kotlin.examples.lesson5.abstraction

abstract class ReportGenerator {
    // Template method — defines the algorithm structure
    fun generate(): String {
        return header() + "\n" + body() + "\n" + footer()
    }

    // Abstract — each subclass MUST provide its own
    abstract fun header(): String

    abstract fun body(): String

    // Open — has default, subclasses CAN override
    open fun footer(): String = "--- End ---"
}

class HtmlReport : ReportGenerator() {
    override fun header() = "<h1>Report</h1>"

    override fun body() = "<p>HTML content</p>"

    override fun footer() = "<footer>Custom HTML footer</footer>"
}

class TextReport : ReportGenerator() {
    override fun header() = "=== Report ==="

    override fun body() = "Plain text content"
    // Uses default footer
}

fun main() {
    println(HtmlReport().generate())
    println()
    println(TextReport().generate())
}
