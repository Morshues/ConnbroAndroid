package com.morshues.connbroandroid.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "mid_name") val midName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "nick_name") val nickName: String,
    @ColumnInfo(name = "birthday") val birthday: Date?
)