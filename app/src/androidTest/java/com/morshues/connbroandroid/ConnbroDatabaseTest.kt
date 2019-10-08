package com.morshues.connbroandroid

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.morshues.connbroandroid.db.ConnbroDatabase
import com.morshues.connbroandroid.db.model.Characteristic
import com.morshues.connbroandroid.db.model.Person
import com.morshues.connbroandroid.db.model.User
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.sql.Date

@RunWith(AndroidJUnit4::class)
class ConnbroDatabaseTest {
    private lateinit var db: ConnbroDatabase
    private lateinit var testUser: User

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            ConnbroDatabase::class.java
        ).allowMainThreadQueries().build()

        db.userDao().let {
            val userId = it.insert(User(
                account = "aa",
                password = "bb"
            ))
            testUser = it.get(userId)
        }
    }

    @Test
    fun should_Insert_User_Item() {
        val user = User(
            account = "cc",
            password = "dd"
        )
        db.userDao().let {
            val id = it.insert(user)
            val userTest = it.get(id)
            Assert.assertEquals(user.account, userTest.account)
        }
    }

    @Test
    fun should_Insert_Person_Item() {
        val person = Person(
            userId = testUser.id,
            firstName = "David",
            midName = "",
            lastName = "Jackson",
            nickName = "DJ",
            birthday = Date(1985, 3, 8)
        )
        db.personDao().let {
            val id = it.insert(person)
            val personTest = it.get(id)
            Assert.assertEquals(person.firstName, personTest.firstName)
        }
    }

    @Test
    fun should_Insert_Characteristic_Item() {
        val person = Person(
            userId = testUser.id,
            firstName = "David",
            midName = "",
            lastName = "Jackson",
            nickName = "DJ",
            birthday = Date(1985, 3, 8)
        )
        val personId = db.personDao().insert(person)

        val c1 = Characteristic(
            userId = testUser.id,
            description = "Love to eat",
            personId = personId
        )
        val c2 = Characteristic(
            userId = testUser.id,
            description = "Flat fire",
            personId = personId
        )
        db.charDao().let {
            val c1Id = it.insert(c1)
            val charTest1 = it.get(c1Id)
            Assert.assertEquals(c1.description, charTest1.description)
            val c2Id = it.insert(c2)
            val charTest2 = it.get(c2Id)
            Assert.assertEquals(c2.description, charTest2.description)
            val charTest3 = it.getAll()
            Assert.assertTrue(charTest3.size == 2)
            val charTest4 = it.getByPerson(personId)
            Assert.assertTrue(charTest4.size == 2)
        }
        val ppp = db.personDao().getPersonWith(personId)

    }
}