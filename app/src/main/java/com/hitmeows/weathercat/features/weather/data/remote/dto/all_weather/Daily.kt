package com.hitmeows.weathercat.features.weather.data.remote.dto.all_weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Daily(
    val dt: Int,
    val sunrise: Int,
    val sunset: Int,
    val moonrise: Int,
    val moonset: Int,
    @SerialName("moon_phase")
    val moonPhase: Double,
    val temp: Temp,
    @SerialName("feels_like")
    val feelsLike: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    @SerialName("dew_point")
    val dewPoint: Double,
    @SerialName("wind_speed")
    val windSpeed: Double,
    @SerialName("wind_deg")
    val windDeg: Int,
    @SerialName("wind_gust")
    val windGust: Double,
    val weather: List<WeatherX>,
    val clouds: Int,
    val pop: Int,
    val uvi: Double,
    val rain: Double
)