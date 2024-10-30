package com.event.dicodingeventapp.ui.notifications

import androidx.lifecycle.ViewModel
import com.event.dicodingeventapp.data.EventRepository
import com.event.dicodingeventapp.data.local.entity.EventEntity

class NotificationsViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getFinishedEvents() = eventRepository.getFinishedEvents()


    fun saveBookmark(event: EventEntity) {
        eventRepository.setEventBookmark(event, true)
    }

    fun deleteBookmark(event: EventEntity) {
        eventRepository.setEventBookmark(event, false)
    }
}