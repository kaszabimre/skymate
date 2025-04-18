package io.kaszabimre.skymate.util

import kotlinx.datetime.LocalDate

fun formatDate(isoDate: String): String = try {
    val parsed = LocalDate.parse(isoDate)
    parsed.toString()
    "${parsed.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${parsed.dayOfMonth}"
} catch (@Suppress("SwallowedException", "TooGenericExceptionCaught") e: Exception) {
    isoDate // fallback
}
