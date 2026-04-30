package space.harbour.kotlin.examples.lesson2

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class UniversityCourse(
    val subject: String,
    val startDate: LocalDate = LocalDate.now(),
    private var endDate: LocalDate = LocalDate.now().plusDays(7),
    private val students: MutableList<String> = mutableListOf(), // initial value of empty list
) {
    lateinit var code: String

    constructor(
        subject: String,
        startDate: String,
        endDate: String,
    ) : this(
        subject = subject,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate),
    )

    init {
        require(startDate.isBefore(endDate)) { "Start date must be before end date or start date" }
        code = subject
    }

    init {
        println("Starting course: $startDate")
    }

//    val courseLength = ChronoUnit.DAYS.between(startDate, endDate)

    fun courseLength() = ChronoUnit.DAYS.between(startDate, endDate)

    val courseLength get() = ChronoUnit.DAYS.between(startDate, endDate)

    var difficulty = 5
        private set

    // class field which is not part of the constructor
    private var isOpen: Boolean = false // initial value of false

    override fun toString(): String {
        return "subject: $subject " +
            "\nstartDate: $startDate " +
            "\nendDate: $endDate " +
            "\nisOpen: $isOpen " +
            "\nstudents: $students"
    }

    fun addStudent(studentName: String) {
        if (isOpen) {
            students.add(studentName)
        } else {
            error("Cannot add students to closed course.")
        }
    }

    fun open() {
        isOpen = true
    }

    fun close() {
        isOpen = false
    }

    fun bumpDifficulty() {
        difficulty++
    }

    fun moveEndDate(endDate: LocalDate) {
        this.endDate = endDate
    }

    inner class Lesson(
        val subject: String,
        val difficulty: Int,
    ) {
        val startDate = LocalDate.now()

        fun startLesson(startDate: LocalDate) {
            println(this@UniversityCourse.startDate)
        }
    }
}

fun main(args: Array<String>) {
//    val course = UniversityCourse(
//        subject = "Kotlin",
//        endDate = LocalDate.now().plusDays(20),
//    )
//
//    course.open()
//    course.addStudent("John")
//    println(course.difficulty)
//    course.bumpDifficulty()
//    println(course.courseLength.toString())

    val runnable =
        object : Runnable {
            override fun run() {
                println("running...")
            }
        }

    runnable.run()
}
