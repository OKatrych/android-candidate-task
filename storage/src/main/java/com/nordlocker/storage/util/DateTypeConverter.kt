package com.nordlocker.storage.util

import androidx.room.TypeConverter
import java.time.LocalDateTime

internal class DateTypeConverter {
    @TypeConverter
    fun toDate(dateString: String) : LocalDateTime {
        return LocalDateTime.parse(dateString)
    }

    @TypeConverter
    fun toDateString(date: LocalDateTime): String {
        return date.toString()
    }
}
