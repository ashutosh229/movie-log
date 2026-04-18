package com.example.movielog.core.ui.theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val preferences = application.getSharedPreferences(
        PREFS_NAME,
        Application.MODE_PRIVATE
    )

    private val _isDarkMode = MutableStateFlow(
        preferences.getBoolean(KEY_DARK_MODE, false)
    )
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    fun toggleTheme() {
        setDarkMode(!_isDarkMode.value)
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        preferences.edit()
            .putBoolean(KEY_DARK_MODE, enabled)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "theme_preferences"
        private const val KEY_DARK_MODE = "dark_mode"
    }
}
