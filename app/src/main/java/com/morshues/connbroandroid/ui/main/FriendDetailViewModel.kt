package com.morshues.connbroandroid.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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
        friend.firstName = firstName
        mRepository.updatePerson(friend)
    }

    fun updateMidName(midName: String) {
        val friend = friendData.value?.person?: return
        friend.midName = midName
        mRepository.updatePerson(friend)
    }

    fun updateLastName(lastName: String) {
        val friend = friendData.value?.person?: return
        friend.lastName = lastName
        mRepository.updatePerson(friend)
    }

    fun updateNickName(nickName: String) {
        val friend = friendData.value?.person?: return
        friend.nickName = nickName
        mRepository.updatePerson(friend)
    }

    fun updateBirthday(date: Date?) {
        val friend = friendData.value?.person?: return
        friend.birthday = date
        mRepository.updatePerson(friend)
    }

    fun updateNote(note: String) {
        val friend = friendData.value?.person?: return
        friend.note = note
        mRepository.updatePerson(friend)
    }

    fun insertInfo(title: String, description: String) {
        val friend = friendData.value?.person?: return
        val info = PersonalInfo(
            userId = friend.userId,
            personId = friend.id,
            title = title,
            description = description
        )
        mRepository.insertPersonalInfo(info)
    }
}