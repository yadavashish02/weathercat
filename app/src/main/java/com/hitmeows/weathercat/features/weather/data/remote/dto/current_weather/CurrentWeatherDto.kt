package com.hitmeows.weathercat.features.weather.data.remote.dto.current_weather


import kotlinx.serialization.Serializable

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