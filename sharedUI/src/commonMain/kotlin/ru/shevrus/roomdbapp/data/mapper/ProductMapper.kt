package ru.shevrus.roomdbapp.data.mapper

import ru.shevrus.roomdbapp.data.database.ProductEntity
import ru.shevrus.roomdbapp.data.network.model.ProductDto
import ru.shevrus.roomdbapp.domain.model.Product

fun ProductEntity.toDomain() = Product(id, title, description, price, thumbnail)
fun ProductDto.toEntity() = ProductEntity(id, title, description, price, thumbnail)