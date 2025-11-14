package br.com.arml.cep.model.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private fun Timestamp.toFormatted(pattern: String): String{
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    //formatter.timeZone = TimeZone.getTimeZone("UTC")
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(this)
}

fun Timestamp.toFormattedUTC(): String{
    return this.toFormatted("yyyy-MM-dd HH:mm")
}

fun Long.toFormattedBR(): String{
    return Timestamp(this).toFormatted("dd/MM/yyyy")
}

fun Long.addCurrentHour(): Long{
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val currentCalendar = Calendar.getInstance()
    val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
    calendar.add(Calendar.HOUR_OF_DAY, currentHour)
    return calendar.timeInMillis
}