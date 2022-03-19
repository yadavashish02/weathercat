package com.hitmeows.weathercat.features.weather.data.remote.dto.current_weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Clouds(
    val all: Int
)