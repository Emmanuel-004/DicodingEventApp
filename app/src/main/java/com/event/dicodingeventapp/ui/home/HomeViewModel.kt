package com.event.dicodingeventapp.ui.home

import androidx.lifecycle.ViewModel
import com.event.dicodingeventapp.data.EventRepository
import com.event.dicodingeventapp.data.local.entity.EventEntity

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getUpcomingEvents() = eventRepository.getUpcomingEvents()
    fun getFinishedEvents() = eventRepository.getFinishedEvents()

    fun getBookmarkedEvents() = eventRepository.getBookmarkedEvents()

    fun saveBookmark(event: EventEntity) {
        eventRepository.setEventBookmark(event, true)
    }

    fun deleteBookmark(event: EventEntity) {
        eventRepository.setEventBookmark(event, false)
    }

}