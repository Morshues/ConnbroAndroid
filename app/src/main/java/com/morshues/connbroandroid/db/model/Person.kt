package com.morshues.connbroandroid.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    foreignKeys = [
        ForeignKey(entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id", index = true) val userId: Long,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "mid_name") val midName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "nick_name") val nickName: String,
    @ColumnInfo(name = "birthday") val birthday: Date?
)