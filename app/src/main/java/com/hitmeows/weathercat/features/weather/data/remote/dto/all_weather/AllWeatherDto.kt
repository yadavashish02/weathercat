package com.hitmeows.weathercat.features.weather.data.remote.dto.all_weather


import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.data.local.entities.DailyWeather
import com.hitmeows.weathercat.features.weather.data.local.entities.HourlyWeather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class AllWeatherDto(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerialName("timezone_offset")
    val timezoneOffset: Long,
    val hourly: List<Hourly>,
    val daily: List<Daily>
)