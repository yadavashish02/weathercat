package com.hitmeows.weathercat.features.weather.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.hitmeows.weathercat.features.weather.domain.WeatherRepository
import javax.inject.Inject

class UpdateWorker @Inject constructor(
	context: Context,
	params: WorkerParameters,
	private val repository: WeatherRepository
) : CoroutineWorker(context, params) {
	
	override suspend fun doWork(): Result {
		Log.d("okhttp", repository.toString())
		return try {
			repository.update()
			Result.success()
		} catch (e: Exception) {
			Log.d("okhttp", e.message ?: "err")
			if (runAttemptCount < 5) Result.retry()
			else Result.failure(
				workDataOf(
					"err" to e.message
				)
			)
		}
	}
}