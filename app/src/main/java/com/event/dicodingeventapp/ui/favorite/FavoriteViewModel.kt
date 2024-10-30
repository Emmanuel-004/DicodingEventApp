package com.event.dicodingeventapp.ui.favorite

import androidx.lifecycle.ViewModel
import com.event.dicodingeventapp.data.EventRepository
import com.event.dicodingeventapp.data.local.entity.EventEntity

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getBookmarkedEvents() = eventRepository.getBookmarkedEvents()

    fun saveBookmark(event: EventEntity) {
        eventRepository.setEventBookmark(event, true)
    }

    fun deleteBookmark(event: EventEntity) {
        eventRepository.setEventBookmark(event, false)
    }
}