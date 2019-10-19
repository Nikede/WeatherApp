package com.nikede.forecast.data.network

import androidx.lifecycle.LiveData
import com.nikede.forecast.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String
    )
}