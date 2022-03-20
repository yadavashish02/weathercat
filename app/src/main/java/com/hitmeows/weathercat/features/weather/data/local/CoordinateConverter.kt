package com.hitmeows.weathercat.features.weather.data.local

import androidx.room.TypeConverter

class CoordinateConverter {
	@TypeConverter
	fun fromCoordinates(coordinates: Coordinates) = "${coordinates.lat},${coordinates.lon}"
	
	@TypeConverter
	fun toCoordinates(string: String): Coordinates {
		val arr = string.split(',')
		return Coordinates(
			arr[0].toDouble(),
			arr[1].toDouble()
		)
	}
}