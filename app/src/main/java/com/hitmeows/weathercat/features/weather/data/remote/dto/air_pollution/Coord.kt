package com.hitmeows.weathercat.features.weather.data.remote.dto.air_pollution


import kotlinx.serialization.Serializable

@Serializable
data class Coord(
	val lon: Double,
	val lat: Double
)