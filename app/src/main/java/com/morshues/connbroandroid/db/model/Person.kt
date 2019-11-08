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
    @ColumnInfo(name = "user_id", index = true) var userId: Long = 0,
    @ColumnInfo(name = "first_name") var firstName: String,
    @ColumnInfo(name = "mid_name") var midName: String = "",
    @ColumnInfo(name = "last_name") var lastName: String,
    @ColumnInfo(name = "nick_name") var nickName: String,
    @ColumnInfo(name = "birthday") var birthday: Date? = null,
    @ColumnInfo(name = "note") var note: String = ""
) {
    fun fullName(): String {
        return "$firstName $midName $lastName"
    }

    fun showingName(): String {
        val fullName = fullName()
        return when {
            fullName.isBlank() -> nickName
            nickName.isBlank() -> fullName
            else -> "$nickName ($fullName)"
        }
    }
}