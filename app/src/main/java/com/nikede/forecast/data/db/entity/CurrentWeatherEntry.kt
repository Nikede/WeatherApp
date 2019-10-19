package com.nikede.forecast.data.db.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.nikede.forecast.data.db.WeatherDescriptionsConverter

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
@TypeConverters(WeatherDescriptionsConverter::class)
data class CurrentWeatherEntry(
    val feelslike: Double,
    @SerializedName("is_day")
    val isDay: String,
    val precip: Double,
    val temperature: Double,
    val visibility: Double,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("weather_descriptions")
    val weatherDescriptions: List<String>,
    @SerializedName("weather_icons")
    val weatherIcons: List<String>,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_speed")
    val windSpeed: Double
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}