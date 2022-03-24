package com.hitmeows.weathercat.features.weather.data.remote

import com.hitmeows.weathercat.common.HttpRoutes
import com.hitmeows.weathercat.common.Keys
import com.hitmeows.weathercat.common.apiHelper
import com.hitmeows.weathercat.features.weather.data.remote.dto.air_pollution.AirPollutionDto
import com.hitmeows.weathercat.features.weather.data.remote.dto.all_weather.AllWeatherDto
import com.hitmeows.weathercat.features.weather.data.remote.dto.current_weather.CurrentWeatherDto
import io.ktor.client.*
import io.ktor.client.request.*

class WeatherApiImpl(
	private val client: HttpClient
) : WeatherApi {
	companion object {
		const val PARAM_LAT = "lat"
		const val PARAM_LON = "lon"
		const val PARAM_API_KEY = "appid"
		const val PARAM_UNITS = "units"
		const val UNIT_METRIC = "metric"
		const val PARAM_EXCLUDE = "exclude"
		const val EXCLUDE_CURRENT_AND_MINUTELY = "current,minutely"
	}
	
	override suspend fun getCurrentWeather(lat: Double, lon: Double): CurrentWeatherDto {
		return apiHelper {
			client.get {
				url(HttpRoutes.CURRENT_WEATHER)
				parameter(PARAM_LAT, lat)
				parameter(PARAM_LON, lon)
				parameter(PARAM_UNITS, UNIT_METRIC)
				parameter(PARAM_API_KEY, Keys.API_KEY_WEATHERMAP)
			}
		}
	}
	
	override suspend fun getAllWeather(lat: Double, lon: Double): AllWeatherDto {
		return apiHelper {
			client.get {
				url(HttpRoutes.ALL_WEATHER)
				parameter(PARAM_LAT, lat)
				parameter(PARAM_LON, lon)
				parameter(PARAM_UNITS, UNIT_METRIC)
				parameter(PARAM_API_KEY, Keys.API_KEY_WEATHERMAP)
				parameter(PARAM_EXCLUDE, EXCLUDE_CURRENT_AND_MINUTELY)
			}
		}
	}
	
	override suspend fun getPollution(lat: Double, lon: Double): AirPollutionDto {
		return apiHelper {
			client.get {
				url(HttpRoutes.AIR_POLLUTION)
				parameter(PARAM_LAT, lat)
				parameter(PARAM_LON, lon)
				parameter(PARAM_UNITS, UNIT_METRIC)
				parameter(PARAM_API_KEY, Keys.API_KEY_WEATHERMAP)
			}
		}
	}
}