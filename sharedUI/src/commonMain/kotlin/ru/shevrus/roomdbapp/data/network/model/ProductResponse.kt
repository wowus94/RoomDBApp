package ru.shevrus.roomdbapp.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val products: List<ProductDto>,
    val total: Int
)

@Serializable
data class ProductDto(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String
)