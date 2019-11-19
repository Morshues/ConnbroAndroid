package com.morshues.connbroandroid.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.morshues.connbroandroid.db.dao.*
import com.morshues.connbroandroid.db.model.*

@Database(
    entities = [
        User::class,
        Person::class,
        Relationship::class,
        PersonalInfo::class,
        Group::class,
        GroupMember::class,
        Place::class,
        Event::class,
        EventAttendee::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class ConnbroDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun personDao(): PersonDao
    abstract fun personalInfoDao(): PersonalInfoDao
    abstract fun eventDao(): EventDao
    abstract fun eventAttendeeDao(): EventAttendeeDao

    companion object {
        @Volatile
        private var INSTANCE: ConnbroDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ConnbroDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ConnbroDatabase::class.java,
                    "connbro.db"
                ).build()
            }

            return INSTANCE!!
        }
    }
}