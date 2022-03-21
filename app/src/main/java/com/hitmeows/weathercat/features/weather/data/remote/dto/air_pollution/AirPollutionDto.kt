package com.hitmeows.weathercat.features.weather.data.remote.dto.air_pollution


import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.data.local.entities.AirPollution
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AirPollutionDto(
    val coord: Coord,
    val list: List<X>
)