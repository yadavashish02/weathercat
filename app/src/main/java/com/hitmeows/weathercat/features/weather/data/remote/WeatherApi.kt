package com.hitmeows.weathercat.features.weather.data.remote

import com.hitmeows.weathercat.features.weather.data.remote.dto.air_pollution.AirPollutionDto
import com.hitmeows.weathercat.features.weather.data.remote.dto.all_weather.AllWeatherDto
import com.hitmeows.weathercat.features.weather.data.remote.dto.current_weather.CurrentWeatherDto

interface WeatherApi {
	suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherDto
	suspend fun getAllWeather(lat: Double, lon: Double): AllWeatherDto
	suspend fun getPollution(lat: Double, lon: Double): AirPollutionDto
}