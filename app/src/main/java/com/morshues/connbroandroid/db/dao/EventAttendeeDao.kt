package com.morshues.connbroandroid.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.morshues.connbroandroid.db.model.EventAttendee

@Dao
interface EventAttendeeDao {
    @Query("SELECT * FROM event_attendee WHERE id = :id")
    fun get(id: Long): EventAttendee

    @Query("SELECT * FROM event_attendee")
    fun getAll(): List<EventAttendee>

    @Insert
    fun insert(attendee: EventAttendee): Long

    @Delete
    fun delete(attendee: EventAttendee)
}