package com.hitmeows.weathercat.features.country.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Country(
	@PrimaryKey
	val isoCode: String,
	val name: String
)
