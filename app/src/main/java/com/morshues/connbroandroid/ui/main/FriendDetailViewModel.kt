package com.morshues.connbroandroid.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.morshues.connbroandroid.db.model.PersonDetail
import com.morshues.connbroandroid.repo.ConnbroRepository
import java.sql.Date

class FriendDetailViewModel(
    private val mRepository: ConnbroRepository,
    friendId: Long
) : ViewModel() {
    val friendData: LiveData<PersonDetail> = mRepository.getPerson(friendId)

    fun updateBirthday(date: Date?) {
        val friend = friendData.value?.person?: return
        friend.birthday = date
        mRepository.updatePerson(friend)
    }
}
