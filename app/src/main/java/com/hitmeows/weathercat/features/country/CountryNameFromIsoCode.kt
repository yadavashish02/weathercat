package com.hitmeows.weathercat.features.country

import com.hitmeows.weathercat.features.country.data.local.CountryDao

class CountryNameFromIsoCode(
	private val dao: CountryDao
) {
	suspend operator fun invoke(countryCode: String): String {
		return dao.getNameFromCode(countryCode)
	}
	
}