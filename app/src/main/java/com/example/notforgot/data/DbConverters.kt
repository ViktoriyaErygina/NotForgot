package com.example.notforgot.data

import androidx.room.TypeConverter
import java.util.Date

object DbConverters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun priorityToNumber(priority: Priority?): Int = priority?.id ?: 0

    @TypeConverter
    fun numberToPriority(number: Int): Priority? =
        Priority.entries.find { it.id == number }
}