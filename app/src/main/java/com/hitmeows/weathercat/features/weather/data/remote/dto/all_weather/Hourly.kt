package com.hitmeows.weathercat.features.weather.data.remote.dto.all_weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hourly(
    val dt: Int,
    val temp: Double,
    @SerialName("feels_like")
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @SerialName("dew_point")
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    @SerialName("wind_speed")
    val windSpeed: Double,
    @SerialName("wind_deg")
    val windDeg: Int,
    @SerialName("wind_gust")
    val windGust: Double,
    val weather: List<Weather>,
    val pop: Int,
    val rain: Rain
)