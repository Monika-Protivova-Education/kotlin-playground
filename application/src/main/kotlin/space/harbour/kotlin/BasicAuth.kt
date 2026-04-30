package space.harbour.kotlin

import java.util.Base64


fun main() {

    val credentials = "user:password"
    val encodedCredentials = Base64.getEncoder().encodeToString(credentials.toByteArray())
    val authHeader = "Basic $encodedCredentials"
    println(authHeader)
}
