package com.morshues.connbroandroid.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morshues.connbroandroid.db.model.Event
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.db.model.PersonalInfo
import com.morshues.connbroandroid.repo.PersonRepository
import com.morshues.connbroandroid.repo.EventRepository
import com.morshues.connbroandroid.repo.PersonalInfoRepository
import kotlinx.coroutines.launch
import java.util.*

class FriendDetailViewModel(
    private val mRepository: PersonRepository,
    private val eventRepository: EventRepository,
    private val infoRepository: PersonalInfoRepository,
    val userId: Long,
    val friendId: Long
) : ViewModel() {
    val friendData: LiveData<PersonDetail> = mRepository.getPerson(friendId)

    fun updateFirstName(firstName: String) {
        val friend = friendData.value?.person?: return
        friend.firstName = firstName.trim()
        if (friend.showingName().isBlank()) return
        mRepository.updatePerson(friend)
    }

    fun updateMidName(midName: String) {
        val friend = friendData.value?.person?: return
        friend.midName = midName.trim()
        if (friend.showingName().isBlank()) return
        mRepository.updatePerson(friend)
    }

    fun updateLastName(lastName: String) {
        val friend = friendData.value?.person?: return
        friend.lastName = lastName.trim()
        if (friend.showingName().isBlank()) return
        mRepository.updatePerson(friend)
    }

    fun updateNickName(nickName: String) {
        val friend = friendData.value?.person?: return
        friend.nickName = nickName.trim()
        if (friend.showingName().isBlank()) return
        mRepository.updatePerson(friend)
    }

    fun updateBirthday(c: Calendar?) {
        val friend = friendData.value?.person?: return
        friend.birthday = c
        mRepository.updatePerson(friend)
    }

    fun updateNote(note: String) {
        val friend = friendData.value?.person?: return
        friend.note = note.trim()
        mRepository.updatePerson(friend)
    }

    fun deleteInfo(info: PersonalInfo) {
        viewModelScope.launch {
            infoRepository.deletePersonalInfo(info)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.deleteEvent(event)
        }
    }

}