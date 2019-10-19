package com.nikede.forecast.data.db

import androidx.room.TypeConverter

class WeatherDescriptionsConverter {

    @TypeConverter
    fun fromListString(weatherDescriptions: List<String>): String {
        var output = ""
        weatherDescriptions.forEach{
            output += "$it,"
        }
        return output.substring(0, output.lastIndex)
    }

    @TypeConverter
    fun toListString(data: String): List<String> {
        return data.split(",");
    }
}
