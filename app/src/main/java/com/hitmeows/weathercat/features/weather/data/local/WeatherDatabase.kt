package com.hitmeows.weathercat.features.weather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hitmeows.weathercat.features.weather.data.local.entities.*

@Database(
	entities = [
		AirPollution::class,
		CurrentWeather::class,
		DailyWeather::class,
		HourlyWeather::class,
		UserCity::class
	],
	version = 1
)
@TypeConverters(CoordinateConverter::class)
abstract class WeatherDatabase: RoomDatabase() {
	abstract val dao: WeatherDao
	companion object {
		const val DB_NAME = "weather_db"
	}
}