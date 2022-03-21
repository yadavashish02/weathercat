package com.hitmeows.weathercat.features.search.use_cases

data class SearchUseCases(
	val getCity: GetCity,
	val insertUserCity: InsertUserCity,
	val insertCurrentUserCity: InsertCurrentUserCity
)
