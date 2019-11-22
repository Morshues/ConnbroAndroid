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
    @ColumnInfo(name = "user_id", index = true) var userId: Long = 0,
    @ColumnInfo(name = "start_time") var startTime: Date?,
    @ColumnInfo(name = "end_time") var endTime: Date?,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "place_id", index = true) var placeId: Long? = null,
    @ColumnInfo(name = "parent_event_id", index = true) var parentEventId: Long? = null
)