package com.nikede.forecast.data.db.unitlocalized

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.nikede.forecast.data.db.WeatherDescriptionsConverter

@TypeConverters(WeatherDescriptionsConverter::class)
data class CurrentWeatherEntry (
    @ColumnInfo(name = "temperature")
    override val temperature: Double,
    @ColumnInfo(name = "weatherDescriptions")
    override val weatherDescriptions: List<String>,
    @ColumnInfo(name = "weatherIcons")
    override val weatherIcons: List<String>,
    @ColumnInfo(name = "windSpeed")
    override val windSpeed: Double,
    @ColumnInfo(name = "windDir")
    override val windDirection: String,
    @ColumnInfo(name = "precip")
    override val precipitation: Double,
    @ColumnInfo(name = "feelslike")
    override val feelsLike: Double,
    @ColumnInfo(name = "visibility")
    override val visibility: Double
): UnitSpecificCurrentWeatherEntry