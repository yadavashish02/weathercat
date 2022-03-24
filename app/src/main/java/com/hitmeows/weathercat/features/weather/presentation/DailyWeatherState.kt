package com.hitmeows.weathercat.features.weather.presentation

import com.hitmeows.weathercat.features.weather.data.local.entities.DailyWeather

data class DailyWeatherState(
	val isError: Boolean = false,
	val errorMessage: String = "",
	val weather: List<DailyWeather> = emptyList()
)
