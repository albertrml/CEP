package br.com.arml.cep.model.utils

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale


/**
 * Retorna o primeiro instante do dia (00:00:00) para um dado timestamp,
 * no fuso horário especificado (padrão: UTC).
 */
fun Long.toStartOfDay(zoneId: ZoneId = ZoneOffset.UTC): Long {
    return Instant.ofEpochMilli(this)
        .atZone(zoneId)
        .toLocalDate()
        .atStartOfDay(zoneId)
        .toInstant()
        .toEpochMilli()
}

/**
 * Retorna o último instante do dia (23:59:59.999...) para um dado timestamp,
 * no fuso horário especificado (padrão: UTC).
 */
fun Long.toEndOfDay(zoneId: ZoneId = ZoneOffset.UTC): Long {
    return Instant.ofEpochMilli(this)
        .atZone(zoneId)
        .toLocalDate()
        .atTime(LocalTime.MAX)
        .atZone(zoneId)
        .toInstant()
        .toEpochMilli()
}

/**
 * Formata um timestamp (Long em UTC) para uma String de data, considerando o
 * fuso horário especificado (padrão: UTC).
 */
fun Long.toFormattedDate(
    pattern: String = "dd/MM/yyyy",
    zoneId: ZoneId = ZoneOffset.UTC
): String = Instant
    .ofEpochMilli(this) // Interpreta o Long como um momento universal (UTC)
    .atZone(zoneId)     // Aplica o fuso horário desejado
    .format(DateTimeFormatter.ofPattern(pattern)) // Formata a data/hora local resultante

// Funções para formatação de logs, agora padronizadas com o fuso UTC
private fun Timestamp.toFormatted(
    pattern: String,
    locale: Locale = Locale.getDefault(),
): String = this.toInstant()
    .atZone(ZoneOffset.UTC)
    .format(DateTimeFormatter.ofPattern(pattern, locale))
fun Timestamp.toFormattedUTC(): String = this.toFormatted("yyyy-MM-dd HH:mm")

fun Timestamp.toFormattedUTCDate(): String = this.toFormatted("yyyy-MM-dd")
