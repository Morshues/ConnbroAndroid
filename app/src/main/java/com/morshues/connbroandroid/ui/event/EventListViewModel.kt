package com.morshues.connbroandroid.ui.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.morshues.connbroandroid.db.model.Event
import com.morshues.connbroandroid.repo.EventRepository

class EventListViewModel(private val eventsRepository: EventRepository) : ViewModel() {

    val events: LiveData<List<Event>> =
        eventsRepository.getEvents()

}