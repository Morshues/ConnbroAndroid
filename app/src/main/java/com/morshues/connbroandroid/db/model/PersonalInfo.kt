package com.morshues.connbroandroid.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "personal_info",
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
        ForeignKey(entity = Person::class,
            parentColumns = ["id"],
            childColumns = ["source_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ])
data class PersonalInfo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id", index = true) var userId: Long = 0,
    @ColumnInfo(name = "person_id", index = true) var personId: Long = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "source_id", index = true) var sourceId: Long? = null,
    @ColumnInfo(name = "known_at") var knownAt: Calendar? = Calendar.getInstance(),
    @ColumnInfo(name = "discard_at") var discardAt: Calendar? = null
)