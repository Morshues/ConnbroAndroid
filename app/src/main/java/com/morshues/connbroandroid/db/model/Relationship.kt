package com.morshues.connbroandroid.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(
    foreignKeys = [
        ForeignKey(entity = Person::class,
            parentColumns = ["id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Relationship(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "first_met") val firstMet: Date?,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "person_id", index = true) val personId: Long
)