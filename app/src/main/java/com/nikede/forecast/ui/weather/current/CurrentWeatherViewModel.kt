package com.nikede.forecast.ui.weather.current

import androidx.lifecycle.ViewModel
import com.nikede.forecast.data.repository.ForecastRepository
import com.nikede.forecast.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}
