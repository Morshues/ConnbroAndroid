package com.morshues.connbroandroid.db.dao

import androidx.room.*
import com.morshues.connbroandroid.db.model.Event

@Dao
interface EventDao {
    @Query("SELECT * FROM event WHERE id = :id")
    fun get(id: Long): Event

    @Query("SELECT * FROM event")
    fun getAll(): List<Event>

    @Insert
    fun insert(event: Event): Long

    @Update
    fun update(event: Event)

    @Delete
    fun delete(event: Event)
}