package com.hitmeows.weathercat.features.weather.data.local

import androidx.room.*
import com.hitmeows.weathercat.features.weather.data.local.entities.*
import kotlinx.coroutines.flow.Flow
@Dao
interface WeatherDao {
	@Transaction
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertUserCity(userCity: UserCity)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAirPollution(airPollution: AirPollution)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertCurrentWeather(currentWeather: CurrentWeather)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertHourlyWeather(hourlyWeather: HourlyWeather)
	
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertDailyWeather(dailyWeather: DailyWeather)
	
	@Query("select * from airpollution where coordinates = :coordinates")
	fun getAirPollution(coordinates: Coordinates): Flow<AirPollution>
	
	@Query("select * from currentweather where coordinates = :coordinates")
	suspend fun getCurrentWeather(coordinates: Coordinates): CurrentWeather
	
	@Query("select * from hourlyweather where coordinates = :coordinates order by dt asc")
	fun getHourlyWeather(coordinates: Coordinates): Flow<List<HourlyWeather>>
	
	@Query("select * from dailyweather where coordinates = :coordinates order by dt asc")
	fun getDailyWeather(coordinates: Coordinates): Flow<List<DailyWeather>>
	
	@Query("select * from usercity")
	fun getAllUserCities(): Flow<List<UserCity>>
	
	@Query("select count(*) from usercity where coordinates = :coordinates")
	suspend fun countUserCity(coordinates: Coordinates): Int
	
	@Query("select * from usercity where isCurrent = 1")
	suspend fun getCurrentCity(): UserCity
	
	@Query("select count(*) from usercity where isCurrent = 1")
	suspend fun countCurrentCity(): Int
	
	@Delete
	suspend fun deleteUserCity(userCity: UserCity)
	
	@Delete
	suspend fun deleteAirPollution(airPollution: AirPollution)
	
	@Delete
	suspend fun deleteCurrentWeather(currentWeather: CurrentWeather)
	
	@Delete
	suspend fun deleteHourlyWeather(hourlyWeather: List<HourlyWeather>)
	
	@Delete
	suspend fun deleteDailyWeather(dailyWeather: List<DailyWeather>)
	
}