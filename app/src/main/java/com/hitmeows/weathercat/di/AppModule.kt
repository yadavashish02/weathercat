package com.hitmeows.weathercat.di

import android.app.Application
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hitmeows.weathercat.features.country.CountryNameFromIsoCode
import com.hitmeows.weathercat.features.country.data.local.CountryDatabase
import com.hitmeows.weathercat.features.search.data.SearchCityRepositoryImpl
import com.hitmeows.weathercat.features.search.data.remote.SearchApi
import com.hitmeows.weathercat.features.search.data.remote.SearchApiImpl
import com.hitmeows.weathercat.features.search.domain.SearchCityRepository
import com.hitmeows.weathercat.features.search.use_cases.GetCity
import com.hitmeows.weathercat.features.search.use_cases.SearchUseCases
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
				serializer = KotlinxSerializer (
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
	fun provideSearchRepository(db: CountryDatabase ,api: SearchApi): SearchCityRepository {
		return SearchCityRepositoryImpl(db.dao, api)
	}
	
	@Provides
	@Singleton
	fun provideSearchUseCases(repository: SearchCityRepository): SearchUseCases {
		return SearchUseCases(
			GetCity(repository)
		)
	}
	
	@Provides
	@Singleton
	fun provideCountryNameFromIsoCode(db: CountryDatabase): CountryNameFromIsoCode {
		return CountryNameFromIsoCode(db.dao)
	}
	
}