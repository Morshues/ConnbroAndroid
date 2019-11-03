package com.morshues.connbroandroid.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.morshues.connbroandroid.db.model.Person
import com.morshues.connbroandroid.db.model.PersonDetail

@Dao
interface PersonDao {
    @Query("SELECT * FROM person WHERE id = :id")
    fun get(id: Long): LiveData<PersonDetail>

    @Query("SELECT * FROM person")
    fun getAll(): LiveData<List<PersonDetail>>

    @Query("SELECT * FROM person WHERE id IN (:personIds)")
    fun loadAllByIds(personIds: LongArray): LiveData<List<Person>>

    @Query("SELECT * FROM person WHERE first_name LIKE :name")
    fun findByName(name: String): LiveData<List<Person>>

    @Insert
    fun insert(person: Person): Long

    @Delete
    fun delete(user: Person)
}