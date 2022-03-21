package com.hitmeows.weathercat.features.weather.data.remote.dto.air_pollution


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Components(
    val co: Double,
    val no: Double,
    val no2: Double,
    val o3: Double,
    val so2: Double,
    @SerialName("pm2_5")
    val pm25: Double,
    val pm10: Double,
    val nh3: Double
)