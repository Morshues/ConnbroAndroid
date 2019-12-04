package com.morshues.connbroandroid.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.morshues.connbroandroid.db.model.PersonalInfo

@Dao
interface PersonalInfoDao {
    @Query("SELECT * FROM personal_info WHERE id = :id")
    fun get(id: Long): LiveData<PersonalInfo>

    @Query("SELECT * FROM personal_info")
    fun getAll(): List<PersonalInfo>

    @Query("SELECT * FROM personal_info WHERE person_id IN (:personId)")
    fun getByPerson(personId: Long): List<PersonalInfo>

    @Insert
    fun insertSync(info: PersonalInfo): Long

    @Update
    suspend fun update(info: PersonalInfo)

    @Delete
    suspend fun delete(info: PersonalInfo)
}