package com.hitmeows.weathercat.di

import android.app.Application
import androidx.room.Room
import com.hitmeows.weathercat.features.city_list.use_cases.GetUserCitiesWithWeather
import com.hitmeows.weathercat.features.country.CountryNameFromIsoCode
import com.hitmeows.weathercat.features.country.data.local.CountryDatabase
import com.hitmeows.weathercat.features.search.data.SearchCityRepositoryImpl
import com.hitmeows.weathercat.features.search.data.remote.SearchApi
import com.hitmeows.weathercat.features.search.data.remote.SearchApiImpl
import com.hitmeows.weathercat.features.search.domain.SearchCityRepository
import com.hitmeows.weathercat.features.search.use_cases.GetCity
import com.hitmeows.weathercat.features.search.use_cases.InsertCurrentUserCity
import com.hitmeows.weathercat.features.search.use_cases.InsertUserCity
import com.hitmeows.weathercat.features.search.use_cases.SearchUseCases
import com.hitmeows.weathercat.features.weather.data.WeatherRepositoryImpl
import com.hitmeows.weathercat.features.weather.data.local.WeatherDatabase
import com.hitmeows.weathercat.features.weather.data.remote.WeatherApi
import com.hitmeows.weathercat.features.weather.data.remote.WeatherApiImpl
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository
import com.hitmeows.weathercat.features.weather.use_cases.GetAllUserCities
import com.hitmeows.weathercat.features.weather.use_cases.GetCurrent
import com.hitmeows.weathercat.features.weather.use_cases.Insert
import com.hitmeows.weathercat.features.weather.use_cases.WeatherUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {
	@Provides
	@Singleton
	fun provideCountiesDatabase(application: Application): CountryDatabase {
		return Room.databaseBuilder(
			application,
			CountryDatabase::class.java,
			CountryDatabase.DB_NAME
		).createFromAsset("countries.db")
			.build()
	}
	
	@Provides
	@Singleton
	fun provideHttpClient(): HttpClient {
		return HttpClient(Android) {
			install(JsonFeature) {
				serializer = KotlinxSerializer(
					kotlinx.serialization.json.Json {
						isLenient = true
						ignoreUnknownKeys = true
					}
				)
			}
		}
	}
	
	@Provides
	@Singleton
	fun provideSearchApi(client: HttpClient): SearchApi {
		return SearchApiImpl(client)
	}
	
	@Provides
	@Singleton
	fun provideSearchRepository(db: CountryDatabase, api: SearchApi): SearchCityRepository {
		return SearchCityRepositoryImpl(db.dao, api)
	}
	
	@Provides
	@Singleton
	fun provideSearchUseCases(
		repository: SearchCityRepository,
		weatherUseCases: WeatherUseCases
	): SearchUseCases {
		return SearchUseCases(
			GetCity(repository),
			InsertUserCity(weatherUseCases.insert),
			InsertCurrentUserCity(repository, weatherUseCases.insert)
		)
	}
	
	@Provides
	@Singleton
	fun provideCountryNameFromIsoCode(db: CountryDatabase): CountryNameFromIsoCode {
		return CountryNameFromIsoCode(db.dao)
	}
	
	@Provides
	@Singleton
	fun provideWeatherDatabase(application: Application): WeatherDatabase {
		return Room.databaseBuilder(
			application,
			WeatherDatabase::class.java,
			WeatherDatabase.DB_NAME
		).build()
	}
	
	@Provides
	@Singleton
	fun provideWeatherApi(client: HttpClient): WeatherApi {
		return WeatherApiImpl(client)
	}
	
	@Provides
	@Singleton
	fun provideWeatherRepository(db: WeatherDatabase, api: WeatherApi): WeatherRepository {
		return WeatherRepositoryImpl(db, api)
	}
	
	@Provides
	@Singleton
	fun provideWeatherUseCases(repo: WeatherRepository): WeatherUseCases {
		return WeatherUseCases(
			GetCurrent(repo),
			GetAllUserCities(repo),
			Insert(repo)
		)
	}
	
	@Provides
	@Singleton
	fun provideCityListUseCase(weatherUseCases: WeatherUseCases): GetUserCitiesWithWeather {
		return GetUserCitiesWithWeather(
			weatherUseCases.getAllUserCities,
			weatherUseCases.getCurrent
		)
	}
	
}