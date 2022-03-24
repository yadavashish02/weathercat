package com.hitmeows.weathercat.features.weather.data.remote.dto.all_weather


import kotlinx.serialization.Serializable

@Serializable
data class WeatherX(
	val id: Int,
	val main: String,
	val description: String,
	val icon: String
)