package com.hitmeows.weathercat.features.weather.data.remote.dto.all_weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllWeatherDto(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerialName("timezone_offset")
    val timezoneOffset: Int,
    val hourly: List<Hourly>,
    val daily: List<Daily>
)