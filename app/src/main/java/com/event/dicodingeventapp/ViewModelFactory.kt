package com.event.dicodingeventapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.event.dicodingeventapp.data.EventRepository
import com.event.dicodingeventapp.di.Injection
import com.event.dicodingeventapp.ui.dashboard.DashboardViewModel
import com.event.dicodingeventapp.ui.favorite.FavoriteViewModel
import com.event.dicodingeventapp.ui.home.HomeViewModel
import com.event.dicodingeventapp.ui.notifications.NotificationsViewModel
import com.event.dicodingeventapp.ui.settings.SettingPreferences
import com.event.dicodingeventapp.ui.settings.SettingsViewModel

class ViewModelFactory private constructor(private val eventRepository: EventRepository, private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(eventRepository) as T
        }else if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(eventRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(eventRepository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(eventRepository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(eventRepository) as T
        } else if(modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(pref) as T
        } else if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context : Context, preferences: SettingPreferences ): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    preferences
                )
            }.also { instance = it }
    }
}