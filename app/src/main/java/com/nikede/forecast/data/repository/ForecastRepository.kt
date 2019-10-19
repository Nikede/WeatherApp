package com.nikede.forecast.data.repository

import androidx.lifecycle.LiveData
import com.nikede.forecast.data.db.entity.WeatherLocation
import com.nikede.forecast.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<out UnitSpecificCurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}