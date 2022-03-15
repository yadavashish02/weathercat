package com.hitmeows.weathercat.di

import android.app.Application
import androidx.room.Room
import com.hitmeows.weathercat.features.country.data.local.CountryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
}