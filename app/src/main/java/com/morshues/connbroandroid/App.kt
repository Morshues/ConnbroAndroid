package com.morshues.connbroandroid

import android.app.Application
import com.morshues.connbroandroid.reminder.ReminderUtils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ReminderUtils.init(this)
    }

}