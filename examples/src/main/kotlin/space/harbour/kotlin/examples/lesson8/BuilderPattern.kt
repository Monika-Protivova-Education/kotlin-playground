package space.harbour.kotlin.examples.lesson8

data class Email(
    val to: String,
    val subject: String = "",
    val body: String = "",
    val cc: List<String> = emptyList(),
    val attachments: List<String> = emptyList()
)

class EmailBuilder {

    private lateinit var to: String
    private lateinit var subject: String
    private lateinit var body: String
    private var cc: List<String> = emptyList()
    private var attachments: List<String> = emptyList()

    fun recipient(to: String, cc: String): EmailBuilder {
        this.to = to
        this.cc = listOf(cc)
        return this
    }

    fun to(to: String): EmailBuilder {
        this.to = to
        return this
    }

    fun subject(subject: String): EmailBuilder {
        this.subject = subject
        return this
    }

    fun body(body: String): EmailBuilder {
        this.body = body
        return this
    }

    fun cc(c: List<String>): EmailBuilder {
        this.cc = c
        return this
    }

    fun attachments(attachments: List<String>): EmailBuilder {
        this.attachments = attachments
        return this
    }

    fun build() = Email(
        to = this.to,
        subject = this.subject,
        body = this.body,
        cc = this.cc,
        attachments = this.attachments
    )

}

fun main() {
    val emailBuilder = EmailBuilder()

    emailBuilder.to("somebody@harbour.space")
        .subject("Class details")
        .body("Hello")

    val email = emailBuilder.build()

//    val emailBuilder2 = EmailBuilder()
//        .to("somebody@harbour.space")
//        .subject("Class details")
//        .cc(listOf("somebody@harbour.space"))
//        .body("Hello")
//
//    val email2 = emailBuilder2.build()

    val email2 = EmailBuilder()
        .body("Hello")
        .cc(listOf("somebody@harbour.space"))
        .subject("Class details")
        .to("somebody@harbour.space")
        .build()

    println(email)
    println(email2)
}
