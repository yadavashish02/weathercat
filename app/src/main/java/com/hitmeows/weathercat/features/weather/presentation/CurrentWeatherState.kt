package com.hitmeows.weathercat.features.weather.presentation

import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.data.local.entities.CurrentWeather

data class CurrentWeatherState(
	val isError: Boolean = false,
	val errorMessage: String = "",
	val weather: CurrentWeather = CurrentWeather(
		Coordinates(0.0, 0.0),
		0,
		0, 0, 0, "", 0,
		0.0, 0.0, 0.0, 0.0, 0L, 0L, "01d"
	)
)
