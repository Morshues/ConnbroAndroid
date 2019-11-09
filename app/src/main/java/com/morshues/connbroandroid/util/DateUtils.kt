package com.morshues.connbroandroid.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun toCalender(dateStr: CharSequence): Calendar {
        val sdf = SimpleDateFormat.getDateInstance()
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dateStr.toString())
        } catch (e: ParseException) {
        }
        return c
    }

    fun toString(year: Int, month: Int, dayOfMonth: Int): String {
        val birthDate = GregorianCalendar(year, month, dayOfMonth)
        val sdf = SimpleDateFormat.getDateInstance()
        return sdf.format(birthDate.time)
    }

    fun toSqlDate(year: Int, month: Int, dayOfMonth: Int): java.sql.Date {
        val birthDate = GregorianCalendar(year, month, dayOfMonth)
        return java.sql.Date(birthDate.timeInMillis)
    }

    fun toSqlDate(dateStr: CharSequence): java.sql.Date? {
        val sdf = SimpleDateFormat.getDateInstance()
        return try {
            val date = sdf.parse(dateStr.toString())
            java.sql.Date(date.time)
        } catch (e: ParseException) {
            null
        }
    }

}