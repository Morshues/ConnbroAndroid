package com.morshues.connbroandroid.ui.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morshues.connbroandroid.db.model.Event
import com.morshues.connbroandroid.repo.EventRepository
import kotlinx.coroutines.launch

class EventEditingViewModel(
    private val eventRepository: EventRepository,
    val eventId: Long,
    private val userId: Long,
    private val friendId: Long
) : ViewModel() {
    val event = eventRepository.getEvent(eventId)

    fun addEvent(event: Event) {
        event.userId = userId
        viewModelScope.launch {
            eventRepository.insertEvent(event, userId, friendId)
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.updateEvent(event)
        }
    }
}