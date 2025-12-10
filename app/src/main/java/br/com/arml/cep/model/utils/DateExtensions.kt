package br.com.arml.cep.model.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Locale
import java.util.TimeZone

private fun Timestamp.toFormatted(pattern: String): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(this)
}

fun Timestamp.toFormattedUTC(): String = this.toFormatted("yyyy-MM-dd HH:mm")

fun Long.toFormattedBR(): String = Timestamp(this).toFormatted("dd/MM/yyyy")

fun Long.adjustDay(): Long = Instant.ofEpochMilli(this)
    .atZone(ZoneOffset.UTC)
    .toLocalDate()
    .atStartOfDay(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()