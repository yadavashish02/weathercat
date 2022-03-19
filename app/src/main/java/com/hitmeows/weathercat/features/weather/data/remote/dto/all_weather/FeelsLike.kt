package com.hitmeows.weathercat.features.weather.data.remote.dto.all_weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)