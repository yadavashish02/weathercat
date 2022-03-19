package com.hitmeows.weathercat.features.weather.data.remote.dto.air_pollution


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AirPollutionDto(
    val coord: Coord,
    val list: List<X>
)