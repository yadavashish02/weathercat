package com.hitmeows.weathercat.features.weather.data

import androidx.room.withTransaction
import com.hitmeows.weathercat.features.weather.data.local.Coordinates
import com.hitmeows.weathercat.features.weather.data.local.WeatherDatabase
import com.hitmeows.weathercat.features.weather.data.local.entities.*
import com.hitmeows.weathercat.features.weather.data.remote.WeatherApi
import com.hitmeows.weathercat.features.weather.data.remote.dto.air_pollution.AirPollutionDto
import com.hitmeows.weathercat.features.weather.data.remote.dto.all_weather.AllWeatherDto
import com.hitmeows.weathercat.features.weather.data.remote.dto.current_weather.CurrentWeatherDto
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

class WeatherRepositoryImpl(
	private val db: WeatherDatabase,
	private val api: WeatherApi
) : WeatherRepository {
	private val dao = db.dao
	
	override suspend fun insertUserCityWithWeather(userCity: UserCity) {
		if (dao.countUserCity(userCity.coordinates) > 0) return
		if (userCity.isCurrent && dao.countCurrentCity()>0) {
			dao.deleteUserCity(dao.getCurrentCity())
		}
		db.withTransaction {
			dao.insertUserCity(userCity)
			dao.insertAirPollution(
				api.getPollution(
					userCity.coordinates.lat,
					userCity.coordinates.lon
				).toAirPollution(userCity.coordinates)
			)
			
			dao.insertCurrentWeather(
				api.getCurrentWeather(
					userCity.coordinates.lat,
					userCity.coordinates.lon
				).toCurrentWeather(userCity.coordinates)
			)
			
			val allWeather = api.getAllWeather(
				userCity.coordinates.lat,
				userCity.coordinates.lon
			)
			
			allWeather.toHourlyWeather(userCity.coordinates).forEach { hourlyItem ->
				dao.insertHourlyWeather(hourlyItem)
			}
			allWeather.toDailyWeather(userCity.coordinates).forEach { dailyItem ->
				dao.insertDailyWeather(dailyItem)
			}
		}
	}
	
	override suspend fun getAirPollution(coordinates: Coordinates): Flow<AirPollution> {
		return dao.getAirPollution(coordinates)
	}
	
	override suspend fun getCurrentWeather(coordinates: Coordinates): CurrentWeather {
		return dao.getCurrentWeather(coordinates)
	}
	
	override suspend fun getHourlyWeather(coordinates: Coordinates): Flow<List<HourlyWeather>> {
		return dao.getHourlyWeather(coordinates)
	}
	
	override suspend fun getDailyWeather(coordinates: Coordinates): Flow<List<DailyWeather>> {
		return dao.getDailyWeather(coordinates)
	}
	
	override suspend fun deleteUserCity(userCity: UserCity) {
		db.withTransaction {
			dao.getAirPollution(userCity.coordinates).collectLatest {
				dao.deleteAirPollution(it)
			}
			dao.deleteCurrentWeather(dao.getCurrentWeather(userCity.coordinates))
			dao.getHourlyWeather(userCity.coordinates).collectLatest {
				dao.deleteHourlyWeather(it)
			}
			dao.getDailyWeather(userCity.coordinates).collectLatest {
				dao.deleteDailyWeather(it)
			}
			dao.deleteUserCity(userCity)
		}
	}
	
	override suspend fun getAllUserCities(): Flow<List<UserCity>> {
		return dao.getAllUserCities()
	}
	
	private fun AirPollutionDto.toAirPollution(coordinates: Coordinates): AirPollution {
		return AirPollution(
			Coordinates(
				coordinates.lat,
				coordinates.lon
			),
			list[0].main.aqi
		)
	}
	
	private fun AllWeatherDto.toHourlyWeather(coordinates: Coordinates): List<HourlyWeather> {
		val list = mutableListOf<HourlyWeather>()
		hourly.forEach { hourlyItem ->
			list.add(
				HourlyWeather(
					coordinates,
					hourlyItem.temp.roundToInt(),
					hourlyItem.weather[0].icon,
					hourlyItem.dt,
					timezoneOffset
				)
			)
		}
		return list.toList()
	}
	
	private fun AllWeatherDto.toDailyWeather(coordinates: Coordinates): List<DailyWeather> {
		val list = mutableListOf<DailyWeather>()
		daily.forEach { dailyItem ->
			list.add(
				DailyWeather(
					coordinates,
					dailyItem.temp.min.roundToInt(),
					dailyItem.temp.max.roundToInt(),
					dailyItem.weather[0].icon,
					dailyItem.weather[0].main,
					dailyItem.dt,
					timezoneOffset
				)
			)
		}
		return list.toList()
	}
	
	private fun CurrentWeatherDto.toCurrentWeather(coordinates: Coordinates): CurrentWeather {
		return CurrentWeather(
			coordinates,
			main.temp.roundToInt(),
			main.tempMax.roundToInt(),
			main.tempMin.roundToInt(),
			weather[0].id,
			weather[0].main,
			main.feelsLike.roundToInt(),
			main.pressure,
			main.humidity,
			wind.speed,
			wind.deg,
			dt,
			timezone
		)
	}
}