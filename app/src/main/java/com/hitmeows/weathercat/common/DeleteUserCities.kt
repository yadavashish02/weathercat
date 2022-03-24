package com.hitmeows.weathercat.common

import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository

class DeleteUserCities(
	private val repository: WeatherRepository
) {
	suspend operator fun invoke(coordinates: List<Coordinates>): Resource<Boolean> {
		return try {
			coordinates.forEach { coordinate ->
				repository.deleteUserCity(coordinate)
			}
			Resource.Success(true)
		} catch (e: Exception) {
			Resource.Error(e, false)
		}
	}
}