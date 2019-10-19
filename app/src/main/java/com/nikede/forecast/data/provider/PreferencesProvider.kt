package com.nikede.forecast.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class PreferencesProvider(context: Context) {
    private val appContext = context.applicationContext

    val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)
}