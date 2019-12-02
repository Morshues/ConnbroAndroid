package com.morshues.connbroandroid.db

import androidx.room.TypeConverter
import java.util.*


class DateConverter {

    @TypeConverter fun calendarToDatestamp(calendar: Calendar?): Long? = calendar?.timeInMillis

    @TypeConverter fun datestampToCalendar(value: Long?): Calendar? {
        if (value == null) {
            return null
        }
        return Calendar.getInstance().apply { timeInMillis = value }
    }

}