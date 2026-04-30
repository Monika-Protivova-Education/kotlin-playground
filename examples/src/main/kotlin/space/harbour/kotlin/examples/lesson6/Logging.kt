package space.harbour.kotlin.examples.lesson6

import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger { }

fun main() {
    logger.trace { "development detail" }

    logger.debug { "developer info" }

    logger.info { "just a message" }

    logger.warn { "something funky is happening" }

    logger.error { "there is a problem" }
}
