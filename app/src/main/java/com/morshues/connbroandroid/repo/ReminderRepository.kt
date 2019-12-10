package com.morshues.connbroandroid.repo

import com.morshues.connbroandroid.db.dao.ReminderDao
import com.morshues.connbroandroid.db.model.Reminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReminderRepository(private val reminderDao: ReminderDao)  {

    fun getReminders() = reminderDao.getActiveReminders()

    fun getReminderByEventId(eventId: Long) = reminderDao.getByEventId(eventId)

    suspend fun insertReminder(reminder: Reminder) = withContext(Dispatchers.IO) {
        reminderDao.insert(reminder)
    }

    suspend fun updateReminder(reminder: Reminder) = withContext(Dispatchers.IO) {
        reminderDao.update(reminder)
    }

    suspend fun removeReminder(reminder: Reminder) = withContext(Dispatchers.IO) {
        reminderDao.delete(reminder)
    }

    companion object {
        @Volatile private var instance: ReminderRepository? = null

        fun getInstance(reminderDao: ReminderDao) =
            instance ?: synchronized(this) {
                instance ?: ReminderRepository(reminderDao).also { instance = it }
            }
    }
}