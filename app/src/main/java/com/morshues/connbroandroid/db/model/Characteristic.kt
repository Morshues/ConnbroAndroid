package com.morshues.connbroandroid.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = Person::class,
            parentColumns = ["id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Characteristic(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "is_positive") val isPositive: Boolean? = null,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "person_id", index = true) val personId: Long
)