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
        ),
        ForeignKey(entity = Place::class,
            parentColumns = ["id"],
            childColumns = ["place_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["parent_event_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id", index = true) val userId: Long,
    @ColumnInfo(name = "start_time") val startTime: Date,
    @ColumnInfo(name = "end_time") val endTime: Date,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "place_id", index = true) val placeId: Long,
    @ColumnInfo(name = "parent_event_id", index = true) val parentEventId: Long?
)