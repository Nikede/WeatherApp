package com.nikede.forecast.data.db.unitlocalized

interface UnitSpecificCurrentWeatherEntry {
    val temperature: Double
    val weatherDescriptions: List<String>
    val weatherIcons: List<String>
    val windSpeed: Double
    val windDirection: String
    val precipitation: Double
    val feelsLike: Double
    val visibility: Double
}