package com.morshues.connbroandroid.db.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class PersonDetail(
    @Embedded
    val person: Person,

    @Relation(parentColumn = "id", entityColumn = "person_id", entity = PersonalInfo::class)
    val info: List<PersonalInfo>,

    @Relation(
        parentColumn = "id",
        entity = Event::class,
        entityColumn = "id",
        associateBy = Junction(
            value = EventAttendee::class,
            parentColumn = "person_id",
            entityColumn = "event_id"
        )
    )
    val events: List<Event>

) {
    fun sortedInfo(): List<PersonalInfo> {
        return info.sortedByDescending { it.id }
    }
}