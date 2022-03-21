package com.hitmeows.weathercat.features.weather.use_cases

import com.hitmeows.weathercat.features.weather.data.local.entities.UserCity
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetAllUserCities(
	private val repository: WeatherRepository
) {
	suspend operator fun invoke(): Flow<List<UserCity>> {
		return repository.getAllUserCities()
	}
}