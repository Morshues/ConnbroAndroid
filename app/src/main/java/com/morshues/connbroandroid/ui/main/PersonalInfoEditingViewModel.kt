package com.morshues.connbroandroid.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morshues.connbroandroid.db.model.PersonalInfo
import com.morshues.connbroandroid.repo.PersonalInfoRepository
import kotlinx.coroutines.launch

class PersonalInfoEditingViewModel(
    private val infoRepository: PersonalInfoRepository,
    val infoId: Long,
    private val userId: Long,
    private val friendId: Long
) : ViewModel() {
    val info = infoRepository.getPersonalInfo(infoId)

    fun addPersonalInfo(info: PersonalInfo) {
        info.userId = userId
        info.personId = friendId
        viewModelScope.launch {
            infoRepository.insertPersonalInfo(info)
        }
    }

    fun updatePersonalInfo(info: PersonalInfo) {
        viewModelScope.launch {
            infoRepository.updatePersonalInfo(info)
        }
    }
}