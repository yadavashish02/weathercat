package com.hitmeows.weathercat.common

import android.util.Log
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository

class UpdateWeather(
	private val repository: WeatherRepository
) {
	suspend operator fun invoke(): Resource<Boolean> {
		return try {
			repository.update()
			Resource.Success(true)
		} catch (e: Exception) {
			Resource.Error(e, false)
		}
	}
	
	suspend fun single(lat: Double, lon: Double): Resource<Boolean> {
		return try {
			repository.update(lat, lon)
			Resource.Success(true)
		} catch (e: Exception) {
			Log.d("updateee", e.message ?: "error")
			Resource.Error(e, false)
		}
	}
	
}