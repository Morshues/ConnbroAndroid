package com.morshues.connbroandroid.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.morshues.connbroandroid.db.model.Person

@Dao
interface PersonDao {
    @Query("SELECT * FROM person WHERE id = :id")
    fun get(id: Long): Person

    @Query("SELECT * FROM person")
    fun getAll(): List<Person>

    @Query("SELECT * FROM person WHERE id IN (:personIds)")
    fun loadAllByIds(personIds: LongArray): List<Person>

    @Query("SELECT * FROM person WHERE first_name LIKE :name")
    fun findByName(name: String): List<Person>

    @Insert
    fun insert(person: Person): Long

    @Delete
    fun delete(user: Person)
}