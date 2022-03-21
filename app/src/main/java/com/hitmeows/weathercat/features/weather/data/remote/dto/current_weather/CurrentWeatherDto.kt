package com.hitmeows.weathercat.features.weather.data.remote.dto.current_weather


import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.data.local.entities.CurrentWeather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class CurrentWeatherDto(
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val dt: Long,
    val timezone: Long,
    val id: Int,
    val name: String,
    val cod: Int
)