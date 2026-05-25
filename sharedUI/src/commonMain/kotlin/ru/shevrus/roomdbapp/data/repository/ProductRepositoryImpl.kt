package ru.shevrus.roomdbapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.shevrus.roomdbapp.data.database.ProductDao
import ru.shevrus.roomdbapp.data.mapper.toAppError
import ru.shevrus.roomdbapp.data.mapper.toDomain
import ru.shevrus.roomdbapp.data.mapper.toEntity
import ru.shevrus.roomdbapp.data.network.ProductApi
import ru.shevrus.roomdbapp.domain.model.Product
import ru.shevrus.roomdbapp.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val api: ProductApi,
    private val productDao: ProductDao
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAllProductsAsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun syncProducts(limit: Int, skip: Int): Result<Int> {
        return try {

            val response = api.fetchProducts(limit = limit, skip = skip)

            productDao.insertProducts(response.products.map { it.toEntity() })

            Result.success(response.total)
        } catch (e: Exception) {
            Result.failure(e.toAppError())
        }
    }
}