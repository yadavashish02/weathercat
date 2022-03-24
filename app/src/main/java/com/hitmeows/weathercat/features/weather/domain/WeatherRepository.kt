package com.hitmeows.weathercat.features.weather.domain

import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.data.local.entities.*
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
	suspend fun insertUserCityWithWeather(userCity: UserCity, isUpdate: Boolean = false)
	suspend fun getAirPollution(coordinates: Coordinates): AirPollution
	suspend fun getCurrentWeather(coordinates: Coordinates): CurrentWeather
	suspend fun getHourlyWeather(coordinates: Coordinates): Flow<List<HourlyWeather>>
	suspend fun getDailyWeather(coordinates: Coordinates): Flow<List<DailyWeather>>
	suspend fun deleteUserCity(coordinates: Coordinates)
	suspend fun getAllUserCities(): Flow<List<UserCity>>
	suspend fun update()
	suspend fun update(lat: Double, lon: Double)
}