package com.hitmeows.weathercat.features.weather.presentation

import com.hitmeows.weathercat.features.weather.data.local.entities.HourlyWeather

data class HourlyWeatherState(
	val isError: Boolean = false,
	val errorMessage: String = "",
	val weather: List<HourlyWeather> = emptyList()
)
