package space.harbour.kotlin.error

import java.time.ZonedDateTime

data class AppErrorResponse(
    val message: String,
    val traceId: String,
    val timestamp: ZonedDateTime,
)