package com.morshues.connbroandroid.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.morshues.connbroandroid.db.model.Event
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.db.model.PersonalInfo
import com.morshues.connbroandroid.repo.ConnbroRepository
import java.sql.Date

class FriendDetailViewModel(
    private val mRepository: ConnbroRepository,
    friendId: Long
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

    fun updateBirthday(date: Date?) {
        val friend = friendData.value?.person?: return
        friend.birthday = date
        mRepository.updatePerson(friend)
    }

    fun updateNote(note: String) {
        val friend = friendData.value?.person?: return
        friend.note = note.trim()
        mRepository.updatePerson(friend)
    }

    fun insertInfo(info: PersonalInfo) {
        val friend = friendData.value?.person?: return
        info.userId = friend.userId
        info.personId = friend.id
        mRepository.insertPersonalInfo(info)
    }

    fun updateInfo(info: PersonalInfo) {
        val friend = friendData.value?.person?: return
        info.userId = friend.userId
        info.personId = friend.id
        mRepository.updatePersonalInfo(info)
    }

    fun deleteInfo(info: PersonalInfo) {
        mRepository.deletePersonalInfo(info)
    }

    fun insertEvent(event: Event) {
        val friend = friendData.value?.person?: return
        event.userId = friend.userId
        mRepository.insertEvent(event, friend)
    }

    fun updateEvent(event: Event) {
        val friend = friendData.value?.person?: return
        event.userId = friend.userId
        mRepository.updateEvent(event)
    }

    fun deleteEvent(event: Event) {
        mRepository.deleteEvent(event)
    }

}