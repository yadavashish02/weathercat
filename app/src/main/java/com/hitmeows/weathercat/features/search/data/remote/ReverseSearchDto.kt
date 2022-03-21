package com.hitmeows.weathercat.features.search.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReverseSearchDto(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)