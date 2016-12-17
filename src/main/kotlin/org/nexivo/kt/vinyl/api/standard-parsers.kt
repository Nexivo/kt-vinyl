package org.nexivo.kt.vinyl.api

import java.text.SimpleDateFormat
import java.util.*

const val ARRAY_SPLITTER: String = "\\s+"

val BOOLEAN_MATCHER: Regex = Regex("^\\s*(?:1|yes|true)\\s*$", RegexOption.IGNORE_CASE)

fun ParseBoolean (value: String): Boolean = BOOLEAN_MATCHER.matches(value)

fun ParseDouble  (value: String): Double  = value.toDouble()

fun ParseFloat   (value: String): Float   = value.toFloat()

fun ParseInt     (value: String): Int     = value.toInt()

fun ParseLong    (value: String): Long    = value.toLong()

fun GetCalendarParser(format: String, locale: Locale): (String) -> Calendar {

    val parser: SimpleDateFormat = SimpleDateFormat(format, locale)

    return { GregorianCalendar().apply { time = parser.parse(it) } }
}

