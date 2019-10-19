package com.nikede.forecast.data.network.response


import com.google.gson.annotations.SerializedName
import com.nikede.forecast.data.db.entity.CurrentWeatherEntry
import com.nikede.forecast.data.db.entity.Error
import com.nikede.forecast.data.db.entity.WeatherLocation
import com.nikede.forecast.data.db.entity.Request

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation,
    val request: Request,
    val success: Boolean?,
    val error: Error?
)