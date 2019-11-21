package com.morshues.connbroandroid.util

import java.sql.Date
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

    fun toTimeString(date: Date?): String {
        if (date == null) return ""
        val sdf = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        return sdf.format(date.time)
    }

    fun toSqlDate(year: Int, month: Int, dayOfMonth: Int): Date {
        val birthDate = GregorianCalendar(year, month, dayOfMonth)
        return Date(birthDate.timeInMillis)
    }

    fun toSqlDate(dateStr: CharSequence): Date? {
        val sdf = SimpleDateFormat.getDateInstance()
        return try {
            val date = sdf.parse(dateStr.toString())
            Date(date.time)
        } catch (e: ParseException) {
            null
        }
    }

    fun combineDateTime(date: Date?, time: Date?): Date? {
        val dateCal = GregorianCalendar()
        dateCal.time = date
        val year: Int = dateCal.get(Calendar.YEAR)
        val month: Int = dateCal.get(Calendar.MONTH)
        val day: Int = dateCal.get(Calendar.DAY_OF_MONTH)

        val timeCal = GregorianCalendar()
        timeCal.time = time
        val hour: Int = timeCal.get(Calendar.HOUR_OF_DAY)
        val minute: Int = timeCal.get(Calendar.MINUTE)
        val second = timeCal[Calendar.SECOND]

        val dateTimeCal = GregorianCalendar(year, month, day, hour, minute, second)
        return Date(dateTimeCal.timeInMillis)
    }

}