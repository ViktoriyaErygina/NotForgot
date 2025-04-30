package com.example.notforgot.ui.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

object DateUtils {
    fun millToStringDateFormat(mills: Long) : String {
        val selectedDate = Date(mills)
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        return formatter.format(selectedDate)
    }

    fun zonedDateTimeToString(date: ZonedDateTime?) : String {
        val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        date?.let {
            return dateFormat.format(date)
        } ?: return ""
    }

    fun stringToZonedDateTime(dateString: String) : ZonedDateTime? {
        return try {
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val localDate = LocalDate.parse(dateString, dateFormatter)
            localDate.atStartOfDay(ZoneId.systemDefault())
        } catch (e: Exception) {
            Log.i("DateUtils", "Ошибка преобразования String в ZonedDateTime: ${e.message}")
            null
        }
    }
}