package com.morshues.connbroandroid.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.morshues.connbroandroid.db.ConnbroDatabase
import com.morshues.connbroandroid.repo.PersonRepository
import com.morshues.connbroandroid.repo.EventRepository
import com.morshues.connbroandroid.repo.PersonalInfoRepository
import com.morshues.connbroandroid.repo.ReminderRepository
import com.morshues.connbroandroid.ui.event.EventEditingViewModel
import com.morshues.connbroandroid.ui.event.EventListViewModel
import com.morshues.connbroandroid.ui.main.FriendCreateViewModel
import com.morshues.connbroandroid.ui.main.FriendDetailViewModel
import com.morshues.connbroandroid.ui.main.FriendListViewModel
import com.morshues.connbroandroid.ui.main.PersonalInfoEditingViewModel
import com.morshues.connbroandroid.widget.Frequency
import com.morshues.connbroandroid.widget.FrequencyPickerViewModel

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    @Suppress("UNCHECKED_CAST")
    private inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>):T = f() as T
        }

    private fun getPersonRepository(context: Context): PersonRepository {
        return PersonRepository.getInstance(
            ConnbroDatabase.getInstance(context.applicationContext).userDao(),
            ConnbroDatabase.getInstance(context.applicationContext).personDao()
        )
    }

    private fun getEventRepository(context: Context): EventRepository {
        return EventRepository.getInstance(
            ConnbroDatabase.getInstance(context.applicationContext).eventDao(),
            ConnbroDatabase.getInstance(context.applicationContext).eventAttendeeDao()
        )
    }

    private fun getPersonalInfoRepository(context: Context): PersonalInfoRepository {
        return PersonalInfoRepository.getInstance(
            ConnbroDatabase.getInstance(context.applicationContext).personalInfoDao()
        )
    }

    private fun getReminderRepository(context: Context): ReminderRepository {
        return ReminderRepository.getInstance(
            ConnbroDatabase.getInstance(context.applicationContext).reminderDao()
        )
    }

    fun provideFriendListViewModelFactory(context: Context): ViewModelProvider.Factory {
        val repository = getPersonRepository(context)
        return viewModelFactory { FriendListViewModel(repository) }
    }

    fun provideFriendCreateViewModelFactory(context: Context): ViewModelProvider.Factory {
        val repository = getPersonRepository(context)
        return viewModelFactory { FriendCreateViewModel(repository) }
    }

    fun provideFriendDetailViewModelFactory(
        context: Context,
        userId: Long,
        friendId: Long
    ): ViewModelProvider.Factory {
        val mainRepository = getPersonRepository(context)
        val eventRepository = getEventRepository(context)
        val personalInfoRepository = getPersonalInfoRepository(context)
        return viewModelFactory {
            FriendDetailViewModel(
                mainRepository,
                eventRepository,
                personalInfoRepository,
                userId,
                friendId
            )
        }
    }

    fun provideEventListViewModelFactory(context: Context): ViewModelProvider.Factory {
        val repository = getEventRepository(context)
        return viewModelFactory { EventListViewModel(repository) }
    }

    fun provideEventEditingViewModelFactory(
        context: Context,
        userId: Long,
        friendId: Long,
        eventId: Long
    ): ViewModelProvider.Factory {
        val eventRepository = getEventRepository(context)
        return viewModelFactory {
            EventEditingViewModel(
                eventRepository,
                eventId = eventId,
                userId = userId,
                friendId = friendId
            )
        }
    }

    fun provideFrequencyPickerViewModelFactory(frequency: Frequency): ViewModelProvider.Factory {
        return viewModelFactory { FrequencyPickerViewModel(frequency) }
    }

    fun provideInfoEditingViewModelFactory(
        context: Context,
        userId: Long,
        friendId: Long,
        infoId: Long
    ): ViewModelProvider.Factory {
        val personalInfoRepository = getPersonalInfoRepository(context)
        return viewModelFactory {
            PersonalInfoEditingViewModel(
                personalInfoRepository,
                infoId = infoId,
                userId = userId,
                friendId = friendId
            )
        }
    }
}
