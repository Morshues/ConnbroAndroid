package com.morshues.connbroandroid.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun dateStringToCalender(dateStr: String): Calendar {
        val sdf = SimpleDateFormat.getDateInstance()
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dateStr)
        } catch (e: ParseException) {
        }
        return c
    }

    fun dateCalendarToString(year: Int, month: Int, dayOfMonth: Int): String {
        val birthDate = GregorianCalendar(year, month, dayOfMonth)
        val sdf = SimpleDateFormat.getDateInstance()
        return sdf.format(birthDate.time)
    }

    fun dateStringToSqlDate(dateStr: String): java.sql.Date {
        val sdf = SimpleDateFormat.getDateInstance()
        val date = sdf.parse(dateStr)
        return java.sql.Date(date.time)
    }

}