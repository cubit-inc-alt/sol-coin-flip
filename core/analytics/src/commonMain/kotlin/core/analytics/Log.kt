@file:Suppress("NOTHING_TO_INLINE")

package core.analytics

import io.github.aakira.napier.Napier

object Log {
    inline fun info(message: String) {
        Napier.i(message)
    }

    inline fun info(tag: String, message: String) {
        Napier.i(message, null, tag)
    }

    inline fun debug(message: String) {
        Napier.d(message)
    }

    inline fun debug(tag: String, message: String) {
        Napier.d(message, null, tag)
    }

    inline fun error(message: String) {
        Napier.e(message)
    }

    inline fun error(tag: String, message: String) {
        Napier.e(message, null, tag)
    }

    inline fun error(throwable: Throwable, message: String) {
        Napier.e(message, throwable)
    }

    inline fun error(tag: String, throwable: Throwable, message: String) {
        Napier.e(message, throwable, tag)
    }
}
