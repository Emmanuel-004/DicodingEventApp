package com.event.dicodingeventapp.di

import android.content.Context
import com.event.dicodingeventapp.data.EventRepository
import com.event.dicodingeventapp.data.local.room.EventDatabase
import com.event.dicodingeventapp.data.retrofit.ApiConfig
import com.event.dicodingeventapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }

}