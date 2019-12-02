package com.morshues.connbroandroid.repo

import com.morshues.connbroandroid.db.dao.EventDao

class EventRepository (private val eventDao: EventDao)  {

    fun getEvents() = eventDao.getAll()

    companion object {
        @Volatile private var instance: EventRepository? = null

        fun getInstance(eventDao: EventDao) =
            instance ?: synchronized(this) {
                instance ?: EventRepository(eventDao).also { instance = it }
            }
    }
}