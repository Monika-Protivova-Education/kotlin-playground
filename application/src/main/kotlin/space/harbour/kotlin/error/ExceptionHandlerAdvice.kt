package space.harbour.kotlin.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.ZonedDateTime

@ControllerAdvice
class ExceptionHandlerAdvice {

    @ExceptionHandler(AppError::class)
    fun handleInvalidSate(ex: AppError): ResponseEntity<AppErrorResponse> {

        val status = when (ex) {
            is AppError.InvalidTaskStatus -> HttpStatus.CONFLICT
            is AppError.TaskNotFound -> HttpStatus.NOT_FOUND
            is AppError.Forbidden -> HttpStatus.FORBIDDEN
            is AppError.Unauthorized -> HttpStatus.UNAUTHORIZED
        }

        return ResponseEntity.status(status).body(
            AppErrorResponse(
                message = ex.message ?: "error",
                traceId = System.currentTimeMillis().toString(),
                timestamp = ZonedDateTime.now()
            )
        )
    }

}
