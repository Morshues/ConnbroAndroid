package com.morshues.connbroandroid.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.morshues.connbroandroid.db.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    fun getFirst(): User

    @Query("SELECT * FROM user WHERE id = :id")
    fun get(id: Long): User

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Insert
    fun insert(user: User): Long

    @Delete
    fun delete(user: User)
}