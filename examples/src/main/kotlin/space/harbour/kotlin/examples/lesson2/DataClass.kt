package space.harbour.kotlin.examples.lesson2

import java.time.LocalDate

data class UniversityCourseData(
    val subject: String,
    val startDate: LocalDate = LocalDate.now(),
    var endDate: LocalDate = LocalDate.now().plusDays(7),
    val students: MutableList<String> = mutableListOf(), // initial value of empty list
    val difficulty: Int = 5,
)

fun main() {
    val now = LocalDate.now()

    val course =
        UniversityCourseData(
            subject = "Kotlin",
            startDate = now,
            endDate = now.plusDays(20),
        )

    val course2 = course.copy(subject = "Java")

    val (subject, _, _, students, _) = course

    println(subject)
    println(students)
}
