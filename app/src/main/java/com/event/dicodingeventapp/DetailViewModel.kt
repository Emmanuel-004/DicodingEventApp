package com.event.dicodingeventapp

import androidx.lifecycle.ViewModel
import com.event.dicodingeventapp.data.EventRepository
import com.event.dicodingeventapp.data.local.entity.EventEntity

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel(){
    fun getDetailEvents(eventId: String) = eventRepository.getDetailEvents(eventId)

}