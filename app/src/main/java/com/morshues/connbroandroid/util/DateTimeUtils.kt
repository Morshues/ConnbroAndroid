package com.morshues.connbroandroid.util

import java.lang.NullPointerException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun dateToCalender(dateStr: CharSequence): Calendar? {
        val sdf = SimpleDateFormat.getDateInstance()
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dateStr.toString())!!
        } catch (e: ParseException) {
            return null
        } catch (e: NullPointerException) {
            return null
        }
        return c
    }

    fun dateToCalender(year: Int, month: Int, dayOfMonth: Int): Calendar {
        val c = Calendar.getInstance()
        c.set(Calendar.YEAR, year)
        c.set(Calendar.MONTH, month)
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        return c
    }

    fun timeToCalender(dateStr: CharSequence): Calendar? {
        val sdf = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        val c = Calendar.getInstance()
        try {
            c.time = sdf.parse(dateStr.toString())!!
        } catch (e: ParseException) {
            return null
        } catch (e: NullPointerException) {
            return null
        }
        return c
    }

    fun toDateString(c: Calendar?): String {
        if (c == null) {
            return ""
        }
        val sdf = SimpleDateFormat.getDateInstance()
        return sdf.format(c.time)
    }

    fun toDateString(year: Int, month: Int, dayOfMonth: Int): String {
        val date = GregorianCalendar(year, month, dayOfMonth)
        val sdf = SimpleDateFormat.getDateInstance()
        return sdf.format(date.time)
    }

    fun toTimeString(c: Calendar?): String {
        if (c == null) {
            return ""
        }
        val sdf = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        return sdf.format(c.time)
    }

    fun toTimeString(hourOfDay: Int, minute: Int): String {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)
        val sdf = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
        return sdf.format(c.time)
    }

    fun combineDateTime(dateCal: Calendar?, timeCal: Calendar?): Calendar? {
        val year: Int = dateCal?.get(Calendar.YEAR) ?: 0
        val month: Int = dateCal?.get(Calendar.MONTH) ?: 0
        val day: Int = dateCal?.get(Calendar.DAY_OF_MONTH) ?: 0

        val hour: Int = timeCal?.get(Calendar.HOUR_OF_DAY) ?: 0
        val minute: Int = timeCal?.get(Calendar.MINUTE) ?: 0
        val second = timeCal?.get(Calendar.SECOND) ?: 0

        return GregorianCalendar(year, month, day, hour, minute, second)
    }

}