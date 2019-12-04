package com.morshues.connbroandroid.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.morshues.connbroandroid.db.model.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM event WHERE id = :id")
    fun get(id: Long): LiveData<Event>

    @Query("SELECT * FROM event")
    fun getAll(): LiveData<List<Event>>

    @Insert
    fun insertSync(event: Event): Long

    @Update
    fun updateSync(event: Event)

    @Delete
    suspend fun delete(event: Event)
}