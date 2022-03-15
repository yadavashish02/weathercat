package com.hitmeows.weathercat.features.country

import com.hitmeows.weathercat.features.country.data.local.CountryDao
import com.hitmeows.weathercat.features.country.data.local.CountryDatabase

class CountryNameFromIsoCode(
	private val dao: CountryDao
) {
	suspend operator fun invoke(countryCode: String): String {
		return dao.getNameFromCode(countryCode)
	}
	
}