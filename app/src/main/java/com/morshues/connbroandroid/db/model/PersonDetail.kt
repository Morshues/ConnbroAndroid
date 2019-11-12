package com.morshues.connbroandroid.db.model

import androidx.room.Embedded
import androidx.room.Relation

class PersonDetail(
    @Embedded
    val person: Person,

    @Relation(parentColumn = "id", entityColumn = "person_id", entity = PersonalInfo::class)
    val infos: List<PersonalInfo>
)