package com.hitmeows.weathercat.features.country.data.local

import androidx.room.Database

@Database(
	entities = [Country::class],
	version = 1
)
abstract class CountryDatabase {
	abstract val dao: CountryDao
	companion object {
		const val DB_NAME = "countries_db"
	}
}