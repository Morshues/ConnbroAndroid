package com.morshues.connbroandroid.repo

import androidx.lifecycle.LiveData
import com.morshues.connbroandroid.db.dao.*
import com.morshues.connbroandroid.db.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PersonRepository (
    private val userDao: UserDao,
    private var personDao: PersonDao
){
    private fun getCurrentUser() = userDao.getFirst()

    fun getFriends() = personDao.getAll()

    fun insertPerson(person: Person) = runBlocking {
        launch(Dispatchers.IO) {
            person.userId = getCurrentUser().id
            personDao.insert(person)
        }
    }

    fun updatePerson(person: Person) = runBlocking {
        launch(Dispatchers.IO) {
            personDao.update(person)
        }
    }

    fun getPerson(id: Long): LiveData<PersonDetail> {
        return personDao.get(id)
    }

    companion object {

        @Volatile private var instance: PersonRepository? = null

        fun getInstance(userDao: UserDao, personDao: PersonDao) =
            instance ?: synchronized(this) {
                instance ?: PersonRepository(userDao, personDao).also { instance = it }
            }
    }
}