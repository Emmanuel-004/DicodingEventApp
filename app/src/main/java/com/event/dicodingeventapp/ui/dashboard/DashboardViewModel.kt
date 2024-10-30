package com.event.dicodingeventapp.ui.dashboard

import androidx.lifecycle.ViewModel
import com.event.dicodingeventapp.data.EventRepository
import com.event.dicodingeventapp.data.local.entity.EventEntity

class DashboardViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getUpcomingEvents() = eventRepository.getUpcomingEvents()

    fun saveBookmark(event: EventEntity) {
        eventRepository.setEventBookmark(event, true)
    }

    fun deleteBookmark(event: EventEntity) {
        eventRepository.setEventBookmark(event, false)
    }
}