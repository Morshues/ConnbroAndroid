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
        ),
        ForeignKey(entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class EventAttendee(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "person_id", index = true) val personId: Long,
    @ColumnInfo(name = "event_id", index = true) val parentEventId: Long
)