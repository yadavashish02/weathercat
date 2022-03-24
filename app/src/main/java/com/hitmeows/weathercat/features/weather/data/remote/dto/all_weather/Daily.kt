package com.hitmeows.weathercat.features.weather.data.remote.dto.all_weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Daily(
	val dt: Long,
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
	@SerialName("wind_speed")
	val windSpeed: Double,
	@SerialName("wind_deg")
	val windDeg: Int,
	val weather: List<WeatherX>,
)