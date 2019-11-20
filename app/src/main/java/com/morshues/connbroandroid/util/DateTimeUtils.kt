package com.morshues.connbroandroid.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun dateToCalender(dateStr: CharSequence): Calendar {
        val sdf = SimpleDateFormat.getDateInstance()
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dateStr.toString())
        } catch (e: ParseException) {
        }
        return c
    }

    fun timeToCalender(dateStr: CharSequence): Calendar {
        val sdf = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dateStr.toString())
        } catch (e: ParseException) {
        }
        return c
    }

    fun toDateString(year: Int, month: Int, dayOfMonth: Int): String {
        val date = GregorianCalendar(year, month, dayOfMonth)
        val sdf = SimpleDateFormat.getDateInstance()
        return sdf.format(date.time)
    }

    fun toDateString(date: Date?): String {
        if (date == null) return ""
        val sdf = SimpleDateFormat.getDateInstance()
        return sdf.format(date.time)
    }

    fun toTimeString(hourOfDay: Int, minute: Int): String {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)
        val sdf = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        return sdf.format(c.time)
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