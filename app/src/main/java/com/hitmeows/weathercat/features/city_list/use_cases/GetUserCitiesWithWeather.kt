package com.hitmeows.weathercat.features.city_list.use_cases

import com.hitmeows.weathercat.common.Resource
import com.hitmeows.weathercat.features.city_list.UserCityWithWeather
import com.hitmeows.weathercat.features.weather.use_cases.GetAllUserCities
import com.hitmeows.weathercat.features.weather.use_cases.GetCurrent
import kotlinx.coroutines.flow.channelFlow

class GetUserCitiesWithWeather(
	private val getAllUserCities: GetAllUserCities,
	private val getCurrent: GetCurrent
) {
	suspend operator fun invoke() = channelFlow {
		val list = mutableListOf<UserCityWithWeather>()
		send(Resource.Loading())
		try {
			getAllUserCities.invoke().collect { list1 ->
				list.clear()
				list1.forEach { userCity ->
					val current = getCurrent.invoke(
						userCity.coordinates.lat,
						userCity.coordinates.lon
					)
					val city1 = UserCityWithWeather(
						userCity.name,
						current.weatherId,
						current.weatherDescription,
						current.currentTemp
					)
					if (userCity.isCurrent) list.add(0, city1)
					else list.add(city1)
				}
				send(Resource.Success(list.toList()))
			}
		} catch (e: Exception) {
			send(Resource.Error(e, list.toList()))
		}
	}
}