package ru.shevrus.roomdbapp.data.mapper

import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.io.IOException
import ru.shevrus.roomdbapp.domain.model.AppError

fun Throwable.toAppError(): AppError {
    val message = this.message ?: ""

    return when (this) {
        is ResponseException -> {
            when (this.response.status) {
                HttpStatusCode.NotFound -> AppError.Network.NotFound
                HttpStatusCode.InternalServerError -> AppError.Network.ServerError
                else -> AppError.Network.Unknown
            }
        }

        is IOException -> AppError.Network.NoInternet

        else -> {
            when {
                message.contains("404") -> AppError.Network.NotFound
                message.contains("500") -> AppError.Network.ServerError
                message.contains("Unable to resolve host") ||
                        message.contains("No address") ||
                        message.contains("ConnectException") -> AppError.Network.NoInternet
                else -> AppError.Unknown
            }
        }
    }
}