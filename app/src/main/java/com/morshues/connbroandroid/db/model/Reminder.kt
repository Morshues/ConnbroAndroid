package com.morshues.connbroandroid.db.model

import androidx.room.*
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(entity = Event::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id", index = true) var userId: Long = 0,
    @ColumnInfo(name = "event_id", index = true) var eventId: Long = 0,
    @ColumnInfo(name = "repeat_type") var repeatType: Type,
    @ColumnInfo(name = "period") var period: Int,
    @ColumnInfo(name = "base_time") var baseTime: Calendar,
    @ColumnInfo(name = "next_time") var nextTime: Calendar,
    @ColumnInfo(name = "countdown") var countdown: Int,
    @ColumnInfo(name = "is_unlimited") var isUnlimited: Boolean
) {
    enum class Type(val num: Int) { DAILY(0), WEEKLY(1), MONTHLY(2), YEARLY(3) }
}

class RemindTypeConverter {
    @TypeConverter
    fun typeToInt(type: Reminder.Type): Int = type.num

    @TypeConverter
    fun intToType(num: Int): Reminder.Type {
        return when (num) {
            0 -> Reminder.Type.DAILY
            1 -> Reminder.Type.WEEKLY
            2 -> Reminder.Type.MONTHLY
            3 -> Reminder.Type.YEARLY
            else -> Reminder.Type.DAILY
        }
    }
}