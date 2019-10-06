package com.morshues.connbroandroid.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = Group::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = CASCADE),
        ForeignKey(entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = CASCADE)
    ])
data class GroupMember(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "group_id", index = true) val groupId: Long,
    @ColumnInfo(name = "event_id", index = true) val eventId: Long
)