package com.nikede.forecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nikede.forecast.data.network.response.CurrentWeatherResponse
import com.nikede.forecast.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val weatherstackApiService: WeatherstackApiService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String) {
        try {
            _downloadedCurrentWeather.postValue(
                weatherstackApiService
                    .getCurrentWeather(location)
                    .await()
            )
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}