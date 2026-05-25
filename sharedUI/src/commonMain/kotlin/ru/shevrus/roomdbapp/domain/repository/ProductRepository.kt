package ru.shevrus.roomdbapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.shevrus.roomdbapp.domain.model.Product

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun syncProducts(limit: Int, skip: Int): Result<Int>
}