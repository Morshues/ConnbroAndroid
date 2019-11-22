package com.morshues.connbroandroid.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.morshues.connbroandroid.db.ConnbroDatabase
import com.morshues.connbroandroid.db.dao.*
import com.morshues.connbroandroid.db.model.*
import com.morshues.connbroandroid.reminder.ReminderUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ConnbroRepository(application: Application) {
    private val userDao: UserDao
    private var personDao: PersonDao
    private var personalInfoDao: PersonalInfoDao
    private var eventDao: EventDao
    private var eventAttendeeDao: EventAttendeeDao
    private var friends: LiveData<List<PersonDetail>>

    private lateinit var currentUser: User

    init {
        val database = ConnbroDatabase.getInstance(application)
        userDao = database.userDao()
        personDao = database.personDao()
        personalInfoDao = database.personalInfoDao()
        eventDao = database.eventDao()
        eventAttendeeDao = database.eventAttendeeDao()
        friends = personDao.getAll()

        CoroutineScope(Dispatchers.IO).launch {
            val firstUser = database.userDao().getAll().firstOrNull()
            currentUser = if (firstUser == null) {
                val defaultUser = User(
                    account = "default_user",
                    password = "default_password"
                )
                val userId = database.userDao().insert(defaultUser)
                database.userDao().get(userId)
            } else {
                firstUser
            }
        }
    }

    fun insertPerson(person: Person) = runBlocking {
        launch(Dispatchers.IO) {
            person.userId = currentUser.id
            personDao.insert(person)
        }
    }

    fun updatePerson(person: Person) = runBlocking {
        launch(Dispatchers.IO) {
            personDao.update(person)
        }
    }

    fun getPerson(id: Long): LiveData<PersonDetail> {
        return personDao.get(id)
    }

    fun getAllFriends(): LiveData<List<PersonDetail>> {
        return personDao.getAll()
    }

    fun insertPersonalInfo(info: PersonalInfo) = runBlocking {
        launch(Dispatchers.IO) {
            personalInfoDao.insert(info)
        }
    }

    fun updatePersonalInfo(info: PersonalInfo) = runBlocking {
        launch(Dispatchers.IO) {
            personalInfoDao.update(info)
        }
    }

    fun deletePersonalInfo(info: PersonalInfo) = runBlocking {
        launch(Dispatchers.IO) {
            personalInfoDao.delete(info)
        }
    }

    fun insertEvent(event: Event, friend: Person) = runBlocking {
        launch(Dispatchers.IO) {
            val id = eventDao.insert(event)
            val attendee = EventAttendee(
                userId = friend.userId,
                personId = friend.id,
                eventId = id
            )
            eventAttendeeDao.insert(attendee)
            event.apply {
                startTime?.let {
                    ReminderUtils.addReminder(id.toInt(), it.time, title, description)
                }
            }
        }
    }

    fun updateEvent(event: Event) = runBlocking {
        launch(Dispatchers.IO) {
            eventDao.update(event)
            event.apply {
                startTime?.let {
                    ReminderUtils.updateReminder(id.toInt(), it.time, title, description)
                }
            }
        }
    }

    fun deleteEvent(event: Event) = runBlocking {
        launch(Dispatchers.IO) {
            eventDao.delete(event)
            ReminderUtils.cancelReminder(event.id.toInt())
        }
    }
}