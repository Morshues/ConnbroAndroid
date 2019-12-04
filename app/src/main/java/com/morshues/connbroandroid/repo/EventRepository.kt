package com.morshues.connbroandroid.repo

import com.morshues.connbroandroid.db.dao.EventAttendeeDao
import com.morshues.connbroandroid.db.dao.EventDao
import com.morshues.connbroandroid.db.model.Event
import com.morshues.connbroandroid.db.model.EventAttendee
import com.morshues.connbroandroid.reminder.ReminderUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(
    private val eventDao: EventDao,
    private val eventAttendeeDao: EventAttendeeDao
) {

    fun getEvent(id: Long) = eventDao.get(id)

    fun getEvents() = eventDao.getAll()

    suspend fun insertEvent(event: Event, userId: Long, friendId: Long) = withContext(Dispatchers.IO) {
        val id = eventDao.insertSync(event)
        val attendee = EventAttendee(
            userId = userId,
            personId = friendId,
            eventId = id
        )
        eventAttendeeDao.insert(attendee)
        event.apply {
            startTime?.let {
                ReminderUtils.addReminder(id.toInt(), it.timeInMillis, title, description)
            }
        }
    }

    suspend fun deleteEvent(event: Event) = withContext(Dispatchers.IO) {
        eventDao.delete(event)
        ReminderUtils.cancelReminder(event.id.toInt())
    }

    suspend fun updateEvent(event: Event) = withContext(Dispatchers.IO) {
        eventDao.updateSync(event)
        event.apply {
            startTime?.let {
                ReminderUtils.updateReminder(id.toInt(), it.timeInMillis, title, description)
            }
        }
    }

    companion object {
        @Volatile private var instance: EventRepository? = null

        fun getInstance(eventDao: EventDao, eventAttendeeDao: EventAttendeeDao) =
            instance ?: synchronized(this) {
                instance ?: EventRepository(eventDao, eventAttendeeDao).also { instance = it }
            }
    }
}