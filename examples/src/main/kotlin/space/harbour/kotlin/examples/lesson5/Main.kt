package space.harbour.kotlin.examples.lesson5

open class Auth {

    /**
     * Authorizes a user based on the provided username and password.
     *
     * @param user The username of the user attempting to authorize.
     * @param password The password associated with the specified username.
     * @return A Boolean value indicating whether the authorization was successful.
     */
    open fun authorize(
        user: String,
        password: String,
    ): Boolean {

        val result = user == "Monika" && password == "secret2"

        println("Authorized: $user/***")
        return result
    }
}

class ExtendedAuth : Auth() {
    override fun authorize(
        user: String,
        password: String,
    ): Boolean {
        println("Authorized: $user$password")
        val result = user == "Monika" && password == "secret2"
        return result
    }
}

fun doAuthorize(
    auth: Auth,
    user: String,
    password: String,
): Boolean {
    return auth.authorize(user, password)
}

fun main() {
    val authService: Auth = ExtendedAuth()

    require(doAuthorize(authService, "Monika", "secret")) {
        "User not authorized!"
    }
}
