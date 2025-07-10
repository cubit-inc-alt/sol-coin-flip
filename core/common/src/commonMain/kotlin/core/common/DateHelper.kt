package core.common

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

fun LocalDateTime.formatAsDefault(): String {
    return buildString {
        append(dayOfMonth.toString().padStart(2, '0'))
        append("/")
        append(monthNumber.toString().padStart(2, '0'))
        append("/")
        append(year)
    }
}

fun LocalDateTime.formatAsISO(): String {
    return toInstant(timeZone = TimeZone.UTC).toString()
}
