package com.morshues.connbroandroid.repo

import com.morshues.connbroandroid.db.dao.PersonalInfoDao
import com.morshues.connbroandroid.db.model.PersonalInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersonalInfoRepository(
    private val personalInfoDao: PersonalInfoDao
) {

    fun getPersonalInfo(id: Long) = personalInfoDao.get(id)

    suspend fun insertPersonalInfo(info: PersonalInfo) = withContext(Dispatchers.IO) {
        personalInfoDao.insertSync(info)
    }

    suspend fun updatePersonalInfo(info: PersonalInfo) {
        personalInfoDao.update(info)
    }

    suspend fun deletePersonalInfo(info: PersonalInfo) {
        personalInfoDao.delete(info)
    }

    companion object {
        @Volatile private var instance: PersonalInfoRepository? = null

        fun getInstance(personalInfoDao: PersonalInfoDao) =
            instance ?: synchronized(this) {
                instance ?: PersonalInfoRepository(personalInfoDao).also { instance = it }
            }
    }
}