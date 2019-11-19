package com.morshues.connbroandroid.db.model

import androidx.room.*

@Entity(
    tableName = "event_attendee",
    foreignKeys = [
        ForeignKey(entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
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
    @ColumnInfo(name = "user_id", index = true) val userId: Long,
    @ColumnInfo(name = "person_id", index = true) val personId: Long,
    @ColumnInfo(name = "event_id", index = true) val eventId: Long
)