package com.hitmeows.weathercat.features.weather.use_cases

import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.data.local.entities.CurrentWeather
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

class GetCurrent(
	private val repository: WeatherRepository
) {
	suspend operator fun invoke(lat: Double, lon: Double): CurrentWeather {
		return repository.getCurrentWeather(Coordinates(lat, lon))
	}
}