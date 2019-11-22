package com.morshues.connbroandroid.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.morshues.connbroandroid.App
import java.lang.ref.WeakReference

object ReminderUtils {

    const val ARG_REMINDER_CODE = "argCode"
    const val ARG_REMINDER_TITLE = "argTitle"
    const val ARG_REMINDER_DESCRIPTION = "argDescription"

    private lateinit var contextRef: WeakReference<Context>

    fun init(app: App) {
        contextRef = WeakReference(app)
    }

    fun addReminder(code: Int, time: Long, title: String, description: String) {
        val context = contextRef.get() ?: return
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra(ARG_REMINDER_CODE, code)
        intent.putExtra(ARG_REMINDER_TITLE, title)
        intent.putExtra(ARG_REMINDER_DESCRIPTION, description)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_ONE_SHOT)
        am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun updateReminder(code: Int, time: Long, title: String, description: String) {
        val context = contextRef.get() ?: return
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra(ARG_REMINDER_CODE, code)
        intent.putExtra(ARG_REMINDER_TITLE, title)
        intent.putExtra(ARG_REMINDER_DESCRIPTION, description)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun cancelReminder(code: Int) {
        val context = contextRef.get() ?: return
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra(ARG_REMINDER_CODE, code)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(context, code, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        am.cancel(pendingIntent)
    }

}