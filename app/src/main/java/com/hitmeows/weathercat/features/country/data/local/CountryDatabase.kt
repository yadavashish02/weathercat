package com.hitmeows.weathercat.features.country.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
	entities = [Country::class],
	version = 1
)
abstract class CountryDatabase: RoomDatabase() {
	abstract val dao: CountryDao
	companion object {
		const val DB_NAME = "countries_db"
	}
}