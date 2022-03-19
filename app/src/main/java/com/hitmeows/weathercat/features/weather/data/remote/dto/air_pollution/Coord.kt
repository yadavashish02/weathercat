package com.hitmeows.weathercat.features.weather.data.remote.dto.air_pollution


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lon: Int,
    val lat: Int
)