package com.morshues.connbroandroid.db.dao

import androidx.room.*
import com.morshues.connbroandroid.db.model.PersonalInfo

@Dao
interface PersonalInfoDao {
    @Query("SELECT * FROM personal_info WHERE id = :id")
    fun get(id: Long): PersonalInfo

    @Query("SELECT * FROM personal_info")
    fun getAll(): List<PersonalInfo>

    @Query("SELECT * FROM personal_info WHERE person_id IN (:personId)")
    fun getByPerson(personId: Long): List<PersonalInfo>

    @Insert
    fun insert(info: PersonalInfo): Long

    @Update
    fun update(info: PersonalInfo)

    @Delete
    fun delete(info: PersonalInfo)
}