import java.time.LocalDate

class UniversityCourse(
    val subject: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
) {
    // regular class methods

    companion object { // companion object name is optional
        const val KOTLIN = "Kotlin"
        const val JAVA = "Java"

        fun create(subject: String): UniversityCourse {
            return if (subject == KOTLIN) {
                UniversityCourse(
                    subject = subject,
                    startDate = LocalDate.parse("2026-04-27"),
                    endDate = LocalDate.parse("2026-05-15"),
                )
            } else {
                UniversityCourse(
                    subject = subject,
                    startDate = LocalDate.parse("2025-04-27"),
                    endDate = LocalDate.parse("2025-05-15"),
                )
            }
        }

        fun kotlinCourse(
            startDate: String,
            endDate: String,
        ): UniversityCourse {
            return UniversityCourse(
                subject = KOTLIN,
                startDate = LocalDate.parse(startDate),
                endDate = LocalDate.parse(endDate),
            )
        }

        fun javaCourse(
            startDate: String,
            endDate: String,
        ): UniversityCourse {
            return UniversityCourse(
                subject = JAVA,
                startDate = LocalDate.parse(startDate),
                endDate = LocalDate.parse(endDate),
            )
        }
    }
}

fun main() {
    val course =
        UniversityCourse
            .create(subject = UniversityCourse.KOTLIN)

    val course2 =
        UniversityCourse
            .create(subject = UniversityCourse.JAVA)
}
