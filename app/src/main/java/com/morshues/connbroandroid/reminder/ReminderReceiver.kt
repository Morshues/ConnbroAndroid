package com.morshues.connbroandroid.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.morshues.connbroandroid.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val code = intent.getIntExtra(ReminderUtils.ARG_REMINDER_CODE, -1)
        if (code == -1) return
        val title = intent.getStringExtra(ReminderUtils.ARG_REMINDER_TITLE)
        var description = intent.getStringExtra(ReminderUtils.ARG_REMINDER_DESCRIPTION)
        if (description.isBlank()) {
            description = context.getString(R.string.no_description)
        }
        val builder = NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_event)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_reminder_event_title)
            val descriptionText = context.getString(R.string.channel_reminder_event_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(REMINDER_CHANNEL_ID, name, importance).apply {
                this.description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(code, builder.build())
        }
    }

    companion object {
        private const val REMINDER_CHANNEL_ID = "ReminderChannel"
    }
}