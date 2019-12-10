package com.morshues.connbroandroid.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.morshues.connbroandroid.db.model.Reminder

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder WHERE id = :id")
    fun get(id: Long): LiveData<Reminder>

    @Query("SELECT * FROM reminder WHERE event_id = :eventId")
    fun getByEventId(eventId: Long): LiveData<Reminder>

    @Query("SELECT * FROM reminder")
    fun getAll(): LiveData<List<Reminder>>

    @Query("SELECT * FROM reminder")
    fun getActiveReminders(): List<Reminder>

    @Insert
    fun insert(reminder: Reminder): Long

    @Update
    fun update(reminder: Reminder)

    @Delete
    suspend fun delete(reminder: Reminder)
}