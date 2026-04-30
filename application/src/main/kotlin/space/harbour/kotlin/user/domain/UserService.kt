package space.harbour.kotlin.user.domain

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import space.harbour.kotlin.error.AppError

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun getCurrentUserIdOrThrow(): Long {
        val username = getCurrentUsername()
            ?: throw AppError.Unauthorized()
        return userRepository.findByUsername(username)?.id
            ?: throw AppError.Unauthorized()
    }

    fun getCurrentUsername(): String? {
        val auth = SecurityContextHolder.getContext().authentication ?: return null

        auth.authorities.forEach {
            println(it.authority)
        }

        if (!auth.isAuthenticated || auth.principal == "anonymousUser") return null
        return auth.name
    }

    fun getCurrentUser(): User {
        val username = getCurrentUsername()
            ?: throw AppError.Unauthorized()
        return userRepository.findByUsername(username)
            ?: throw AppError.Unauthorized()
    }
}
