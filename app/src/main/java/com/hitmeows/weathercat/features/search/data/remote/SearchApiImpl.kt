package com.hitmeows.weathercat.features.search.data.remote

import com.hitmeows.weathercat.common.HttpRoutes
import com.hitmeows.weathercat.common.Keys
import com.hitmeows.weathercat.common.apiHelper
import io.ktor.client.*
import io.ktor.client.request.*

class SearchApiImpl(
	private val client: HttpClient
): SearchApi {
	companion object {
		const val PARAM_QUERY = "q"
		const val PARAM_LIMIT = "limit"
		const val LIMIT = 5
		const val PARAM_API_KEY = "appid"
	}
	
	override suspend fun searchCity(query: String): List<SearchedCityDto> {
		return apiHelper {
			client.get {
				url(HttpRoutes.SEARCH_CITY)
				parameter(PARAM_QUERY,query)
				parameter(PARAM_LIMIT, LIMIT)
				parameter(PARAM_API_KEY, Keys.API_KEY_WEATHERMAP)
			}
		}
	}
}