package com.morshues.connbroandroid.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.morshues.connbroandroid.db.model.Person
import com.morshues.connbroandroid.db.model.PersonDetail

@Dao
interface PersonDao {
    @Transaction
    @Query("SELECT * FROM person WHERE id = :id")
    fun get(id: Long): LiveData<PersonDetail>

    @Transaction
    @Query("SELECT * FROM person")
    fun getAll(): LiveData<List<PersonDetail>>

    @Query("SELECT * FROM person WHERE id IN (:personIds)")
    fun loadAllByIds(personIds: LongArray): LiveData<List<Person>>

    @Query("SELECT * FROM person WHERE first_name LIKE :name")
    fun findByName(name: String): LiveData<List<Person>>

    @Insert
    fun insert(person: Person): Long

    @Update
    fun update(person: Person)

    @Delete
    fun delete(user: Person)
}