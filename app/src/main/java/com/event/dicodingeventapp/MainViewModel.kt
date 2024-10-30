package com.event.dicodingeventapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.event.dicodingeventapp.ui.settings.SettingPreferences

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSetting() = pref.getThemeSetting().asLiveData()
}