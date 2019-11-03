package com.morshues.connbroandroid.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.morshues.connbroandroid.App
import com.morshues.connbroandroid.db.model.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NewFriendViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()

    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val mRepository = (application as App).repository

    fun insert(person: Person) {
        uiScope.launch {
            mRepository.insertPerson(person)
        }
    }
}
