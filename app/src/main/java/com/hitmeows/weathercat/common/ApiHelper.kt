package com.hitmeows.weathercat.common

import io.ktor.client.features.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

suspend inline fun <T> apiHelper(
	crossinline request: suspend () -> T
): T {
	return withContext(Dispatchers.IO) {
		ensureActive()
		try {
			request()
		} catch (e: RedirectResponseException) {
			throw ApiException(e.response.status.description)
		} catch (e: ClientRequestException) {
			throw ApiException(e.response.status.description)
		} catch (e: ServerResponseException) {
			throw ApiException(e.response.status.description)
		} catch (e: Exception) {
			throw ApiException(e.localizedMessage?:"unknown error occurred")
		}
	}
}

class ApiException(message: String): Exception(message)