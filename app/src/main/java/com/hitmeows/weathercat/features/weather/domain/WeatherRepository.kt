package com.hitmeows.weathercat.features.weather.domain

import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.data.local.entities.*
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
	suspend fun insertUserCityWithWeather(userCity: UserCity)
	suspend fun getAirPollution(coordinates: Coordinates): Flow<AirPollution>
	suspend fun getCurrentWeather(coordinates: Coordinates): CurrentWeather
	suspend fun getHourlyWeather(coordinates: Coordinates): Flow<List<HourlyWeather>>
	suspend fun getDailyWeather(coordinates: Coordinates): Flow<List<DailyWeather>>
	suspend fun deleteUserCity(userCity: UserCity)
	suspend fun getAllUserCities(): Flow<List<UserCity>>
}