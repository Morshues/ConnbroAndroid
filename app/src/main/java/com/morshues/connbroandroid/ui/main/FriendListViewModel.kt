package com.morshues.connbroandroid.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.morshues.connbroandroid.db.model.Person
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.repo.PersonRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FriendListViewModel(
    private val mRepository: PersonRepository
) : ViewModel() {
    private val viewModelJob = SupervisorJob()

    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val allFriends: LiveData<List<PersonDetail>> = mRepository.getFriends()

    fun insert(person: Person) {
        uiScope.launch {
            mRepository.insertPerson(person)
        }
    }
}