package ru.shevrus.roomdbapp.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.shevrus.roomdbapp.domain.model.Product
import ru.shevrus.roomdbapp.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getProducts()
    }
}