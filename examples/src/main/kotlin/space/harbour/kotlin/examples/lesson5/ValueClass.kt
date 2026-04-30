package space.harbour.kotlin.examples.lesson5

@JvmInline
value class UserId(val id: Long)

@JvmInline
value class OrganizationId(val id: Long)

fun createUser(
    name: String,
    orgId: OrganizationId,
    id: UserId,
) {
}

data class User(
    val id: UserId,
    val name: String,
)

data class Organization(
    val id: OrganizationId,
    val name: String,
)

fun main() {
    val userId = UserId(1L)
    val orgId = OrganizationId(1L)

    createUser(
        "John Doe",
        orgId,
        userId,
    )
}
