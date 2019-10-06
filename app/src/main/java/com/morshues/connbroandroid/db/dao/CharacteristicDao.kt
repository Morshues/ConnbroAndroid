package com.morshues.connbroandroid.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.morshues.connbroandroid.db.model.Characteristic

@Dao
interface CharacteristicDao {
    @Query("SELECT * FROM characteristic WHERE id = :id")
    fun get(id: Long): Characteristic

    @Query("SELECT * FROM characteristic")
    fun getAll(): List<Characteristic>

    @Query("SELECT * FROM characteristic WHERE person_id IN (:personId)")
    fun getByPerson(personId: Long): List<Characteristic>

    @Insert
    fun insert(person: Characteristic): Long

    @Delete
    fun delete(user: Characteristic)
}