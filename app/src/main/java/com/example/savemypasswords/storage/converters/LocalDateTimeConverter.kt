package com.example.savemypasswords.storage.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let {
            return LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}
