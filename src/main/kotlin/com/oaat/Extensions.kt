/*
 * Copyright 2018-2019 OAAT's author : Fred Montariol. Use of this source code is governed by the Apache 2.0 license.
 */

package com.oaat

import java.text.Normalizer
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor
import java.util.Locale

private val daysLookup = (1..31).associate { it.toLong() to getOrdinal(it) }

val englishDateFormatter = DateTimeFormatterBuilder()
        .appendPattern("MMMM")
        .appendLiteral(" ")
        .appendText(ChronoField.DAY_OF_MONTH, daysLookup)
        .appendLiteral(" ")
        .appendPattern("yyyy")
        .toFormatter(Locale.ENGLISH)

fun String.slugify() = toLowerCase()
        .stripAccents()
        .replace("\n".toRegex(), " ")
        .replace("[^a-z\\d\\s]".toRegex(), " ")
        .split(" ")
        .joinToString("-")
        .replace("-+".toRegex(), "-")


fun String.stripAccents() = Normalizer
        .normalize(this, Normalizer.Form.NFD)
        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

fun TemporalAccessor.formatDate() = englishDateFormatter.format(this)

fun getOrdinal(n: Int) = when {
    (n in 11..13) ->  "${n}th"
    (n % 10 == 1) -> "${n}st"
    (n % 10 == 2) -> "${n}nd"
    (n % 10 == 3) -> "${n}rd"
    else -> "${n}th"
}
