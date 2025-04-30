package com.example.notforgot.data

import androidx.room.TypeConverter
import com.example.notforgot.domain.models.Priority
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

object DbConverters {

    @TypeConverter
    fun longToZonedDate(time: Long?): ZonedDateTime? {
        return if (time == null) {
            null
        } else {
            ZonedDateTime.ofInstant(Date(time).toInstant(), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    fun zonedDateToString(date: ZonedDateTime?): Long? {
        return date?.toInstant()?.epochSecond?.times(1000)
    }

    @TypeConverter
    fun priorityToNumber(priority: Priority?): Int = priority?.id ?: 0

    @TypeConverter
    fun numberToPriority(number: Int): Priority? =
        Priority.entries.find { it.id == number }
}