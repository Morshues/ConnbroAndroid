package com.morshues.connbroandroid.util

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.morshues.connbroandroid.db.ConnbroDatabase
import com.morshues.connbroandroid.repo.ConnbroRepository
import com.morshues.connbroandroid.repo.EventRepository
import com.morshues.connbroandroid.ui.event.EventListViewModel
import com.morshues.connbroandroid.ui.main.FriendCreateViewModel
import com.morshues.connbroandroid.ui.main.FriendDetailViewModel
import com.morshues.connbroandroid.ui.main.FriendListViewModel
import com.morshues.connbroandroid.ui.main.viewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    private fun getConnbroRepository(context: Context): ConnbroRepository {
        return ConnbroRepository.getInstance(context)
    }

    private fun getEventRepository(context: Context): EventRepository {
        return EventRepository.getInstance(
            ConnbroDatabase.getInstance(context.applicationContext).eventDao())
    }

    fun provideFriendListViewModelFactory(context: Context): ViewModelProvider.Factory {
        val repository = getConnbroRepository(context)
        return viewModelFactory { FriendListViewModel(repository) }
    }

    fun provideFriendCreateViewModelFactory(context: Context): ViewModelProvider.Factory {
        val repository = getConnbroRepository(context)
        return viewModelFactory { FriendCreateViewModel(repository) }
    }

    fun provideFriendDetailViewModelFactory(
        context: Context,
        friendId: Long
    ): ViewModelProvider.Factory {
        val repository = getConnbroRepository(context)
        return viewModelFactory { FriendDetailViewModel(repository, friendId) }
    }

    fun provideEventListViewModelFactory(context: Context): ViewModelProvider.Factory {
        val repository = getEventRepository(context)
        return viewModelFactory { EventListViewModel(repository) }
    }
}
