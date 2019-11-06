package com.morshues.connbroandroid.ui.main

import androidx.lifecycle.ViewModel
import com.morshues.connbroandroid.db.model.Person
import com.morshues.connbroandroid.repo.ConnbroRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NewFriendViewModel(
    private val mRepository: ConnbroRepository
) : ViewModel() {
    private val viewModelJob = SupervisorJob()

    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun insert(person: Person) {
        uiScope.launch {
            mRepository.insertPerson(person)
        }
    }
}

