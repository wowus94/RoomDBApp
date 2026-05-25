package ru.shevrus.roomdbapp.domain.model

data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String
)