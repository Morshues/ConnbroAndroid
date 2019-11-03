package com.morshues.connbroandroid.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.morshues.connbroandroid.db.ConnbroDatabase
import com.morshues.connbroandroid.db.dao.PersonDao
import com.morshues.connbroandroid.db.dao.UserDao
import com.morshues.connbroandroid.db.model.Person
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.db.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ConnbroRepository(application: Application) {
    private val userDao: UserDao
    private var personDao: PersonDao
    private var friends: LiveData<List<PersonDetail>>

    private lateinit var currentUser: User

    init {
        val database = ConnbroDatabase.getInstance(application)
        userDao = database.userDao()
        personDao = database.personDao()
        friends = personDao.getAll()

        CoroutineScope(Dispatchers.IO).launch {
            val firstUser = database.userDao().getAll().firstOrNull()
            currentUser = if (firstUser == null) {
                val defaultUser = User(
                    account = "default_user",
                    password = "default_password"
                )
                val userId = database.userDao().insert(defaultUser)
                database.userDao().get(userId)
            } else {
                firstUser
            }
        }
    }

    fun insertPerson(person: Person) = runBlocking {
        launch(Dispatchers.IO) {
            person.userId = currentUser.id
            personDao.insert(person)
        }
    }

    fun getAllFriends(): LiveData<List<PersonDetail>> {
        return personDao.getAll()
    }
}