package com.hitmeows.weathercat.features.weather.data.remote.dto.air_pollution


import kotlinx.serialization.Serializable

@Serializable
data class Main(
	val aqi: Int
)