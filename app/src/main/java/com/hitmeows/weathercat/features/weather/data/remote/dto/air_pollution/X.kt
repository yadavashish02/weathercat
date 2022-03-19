package com.hitmeows.weathercat.features.weather.data.remote.dto.air_pollution


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class X(
    val main: Main,
    val components: Components,
    val dt: Int
)