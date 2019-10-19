package com.nikede.forecast.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.nikede.forecast.data.db.CurrentWeatherDao
import com.nikede.forecast.data.db.WeatherLocationDao
import com.nikede.forecast.data.db.entity.WeatherLocation
import com.nikede.forecast.data.provider.LocationProvider
import com.nikede.forecast.data.db.unitlocalized.UnitSpecificCurrentWeatherEntry
import com.nikede.forecast.data.network.WeatherNetworkDataSource
import com.nikede.forecast.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ForecastRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : ForecastRepository {

    private val TAG = "ForecastRepositoryImpl"

    init {
        weatherNetworkDataSource.apply {

            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
        }
    }

    override suspend fun getCurrentWeather(): LiveData<out UnitSpecificCurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDao.getWeather()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<WeatherLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext weatherLocationDao.getLocation()
        }
    }

    @Suppress("UNREACHABLE_CODE")
    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            if (fetchedWeather.success != null && !fetchedWeather.success) {
                if (fetchedWeather.error != null) {
                    Log.e(TAG, fetchedWeather.error.toString())
                }
            }
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
            weatherLocationDao.upsert(fetchedWeather.location)
        }
    }

    suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocation().value

        if (lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            fetchCurrentWeather()
            return
        }

        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime)) {
            fetchCurrentWeather()
        }
    }

    private suspend fun fetchCurrentWeather() {
        weatherNetworkDataSource.fetchCurrentWeather(locationProvider.getPreferredLocationString())
    }

    private fun isFetchCurrentNeeded(lastFetchedTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchedTime.isBefore(thirtyMinutesAgo)
    }
}