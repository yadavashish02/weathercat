package com.hitmeows.weathercat.features.country.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CountryDao {
	@Query("select * from country where isoCode = :isoCode")
	suspend fun getNameFromCode(isoCode: String): String
}