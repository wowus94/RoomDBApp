package ru.shevrus.roomdbapp.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel.BODY
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.shevrus.roomdbapp.data.network.model.ProductResponse

class ProductApi {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = BODY
        }
    }

    private val baseUrl = "https://dummyjson.com"
    var shouldThrowError = false

    suspend fun fetchProducts(limit: Int, skip: Int): ProductResponse {
        if (shouldThrowError) throw Exception("Simulated network failure")

        val response: ProductResponse = httpClient.get("$baseUrl/products") {
            parameter("limit", limit)
            parameter("skip", skip)
        }.body()

        return response
    }
}