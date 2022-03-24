package com.hitmeows.weathercat.features.weather.use_cases

import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository

class DeleteUserCity(
	private val repository: WeatherRepository
) {
	suspend operator fun invoke(coordinates: Coordinates) = repository.deleteUserCity(coordinates)
}